package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.bi.application.commandservices.AdminCommandService;
import com.satecho.agrosafe.platform.apiservice.bi.application.commandservices.FleetCommandService;
import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.AdminQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.FleetQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.exceptions.DiagnosticNotFoundException;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.BatchRegisterDevicesCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.CreateFirmwareCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.ReactivateUserCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.StartDiagnosticCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetDiagnosticQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.resources.MessageResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final AdminCommandService adminCommandService;
    private final AdminQueryService adminQueryService;
    private final FleetCommandService fleetCommandService;
    private final FleetQueryService fleetQueryService;

    public AdminController(AdminCommandService adminCommandService,
                           AdminQueryService adminQueryService,
                           FleetCommandService fleetCommandService,
                           FleetQueryService fleetQueryService) {
        this.adminCommandService = adminCommandService;
        this.adminQueryService = adminQueryService;
        this.fleetCommandService = fleetCommandService;
        this.fleetQueryService = fleetQueryService;
    }

    @GetMapping("/fleet/status")
    public ResponseEntity<FleetHealthResource> getFleetStatus() {
        FleetHealth fleetHealth = fleetQueryService.getFleetHealth();
        FleetHealthResource resource = FleetHealthResourceFromEntityAssembler.toResourceFromEntity(fleetHealth);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/devices/fleet")
    public ResponseEntity<List<DeviceFleetResource>> getFleetDevices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Double batteryLevelBelow,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        List<Device> devices = fleetQueryService.getFleetDevices(status, batteryLevelBelow, limit);
        List<DeviceFleetResource> resources = devices.stream()
                .map(DeviceFleetResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/devices/batch")
    public ResponseEntity<BatchDevicesSummaryResource> batchRegisterDevices(
            @RequestBody BatchRegisterRequest request) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        List<BatchRegisterDevicesCommand.BatchDeviceEntry> entries = request.devices().stream()
                .map(d -> new BatchRegisterDevicesCommand.BatchDeviceEntry(
                        d.serialNumber(), d.type()))
                .toList();
        BatchRegisterDevicesCommand command = new BatchRegisterDevicesCommand(userId, entries);
        var result = fleetCommandService.batchRegisterDevices(command);
        List<String> results = result.isSuccess()
                ? ((Result.Success<List<String>, ApplicationError>) result).value()
                : List.of();

        int succeeded = (int) results.stream().filter(r -> r.startsWith("OK")).count();
        int failed = (int) results.stream().filter(r -> r.startsWith("FAILED")).count();
        List<BatchDevicesSummaryResource.BatchEntryResource> resultEntries = results.stream()
                .map(r -> {
                    if (r.startsWith("OK: ")) {
                        String serial = r.substring(4).split(" - ")[0];
                        return new BatchDevicesSummaryResource.BatchEntryResource(serial, "SUCCESS", null, null);
                    } else {
                        String body = r.substring(8);
                        String serial = body.split(" - ")[0];
                        String error = body.contains(" - ") ? body.substring(body.indexOf(" - ") + 3) : body;
                        return new BatchDevicesSummaryResource.BatchEntryResource(serial, "FAILED", null, error);
                    }
                })
                .toList();

        BatchDevicesSummaryResource summary = new BatchDevicesSummaryResource(
                entries.size(), succeeded, failed, resultEntries);
        return ResponseEntity.status(HttpStatus.CREATED).body(summary);
    }

    @PostMapping("/users/{userId}/suspend")
    public ResponseEntity<MessageResource> suspendUser(
            @PathVariable Long userId,
            @RequestBody SuspendUserResource resource) {
        Long adminId = SecurityContextUtil.getCurrentUserId();
        var command = SuspendUserCommandFromResourceAssembler
                .toCommandFromResource(resource, userId, adminId);
        adminCommandService.suspendUser(command);
        return ResponseEntity.ok(new MessageResource("User suspended successfully"));
    }

    @PostMapping("/users/{userId}/reactivate")
    public ResponseEntity<MessageResource> reactivateUser(@PathVariable Long userId) {
        Long adminId = SecurityContextUtil.getCurrentUserId();
        ReactivateUserCommand command = new ReactivateUserCommand(userId, adminId);
        adminCommandService.reactivateUser(command);
        return ResponseEntity.ok(new MessageResource("User reactivated successfully"));
    }

    @PostMapping("/devices/{deviceId}/diagnostics")
    public ResponseEntity<DiagnosticResource> startDiagnostic(@PathVariable Long deviceId) {
        StartDiagnosticCommand command = new StartDiagnosticCommand(deviceId);
        var result = fleetCommandService.startDiagnostic(command);
        DiagnosticSession session = result.isSuccess()
                ? ((Result.Success<DiagnosticSession, ApplicationError>) result).value()
                : null;
        DiagnosticResource resource = DiagnosticResourceFromEntityAssembler.toResourceFromEntity(session);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(resource);
    }

    @GetMapping("/devices/{deviceId}/diagnostics/{diagnosticId}")
    public ResponseEntity<DiagnosticResource> getDiagnostic(
            @PathVariable Long deviceId,
            @PathVariable Long diagnosticId) {
        GetDiagnosticQuery query = new GetDiagnosticQuery(diagnosticId);
        DiagnosticSession session = fleetQueryService.handle(query)
                .orElseThrow(() -> new DiagnosticNotFoundException(diagnosticId));
        if (!session.getDeviceId().equals(deviceId)) {
            throw new DiagnosticNotFoundException(
                    "Diagnostic " + diagnosticId + " does not belong to device " + deviceId);
        }
        DiagnosticResource resource = DiagnosticResourceFromEntityAssembler.toResourceFromEntity(session);
        return ResponseEntity.ok(resource);
    }

    @PostMapping("/firmware")
    public ResponseEntity<FirmwareResource> createFirmware(
            @RequestBody CreateFirmwareResource request) {
        CreateFirmwareCommand command = CreateFirmwareCommandFromResourceAssembler
                .toCommandFromResource(new FirmwareResource(null, request.version(), request.deviceModel(),
                        request.changelog(), request.binaryUrl(), null, true, null));
        var result = fleetCommandService.createFirmware(command);
        FirmwareRelease release = result.isSuccess()
                ? ((Result.Success<FirmwareRelease, ApplicationError>) result).value()
                : null;
        FirmwareResource resource = FirmwareResourceFromEntityAssembler.toResourceFromEntity(release);
        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResource>> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {
        GetAllUsersQuery query = new GetAllUsersQuery(search, role, page, size);
        Page<User> users = adminQueryService.handle(query);
        List<AdminUserResource> resources = users.getContent().stream()
                .map(AdminUserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminUserResource> getUserById(@PathVariable Long userId) {
        GetUserByIdQuery query = new GetUserByIdQuery(userId);
        User user = adminQueryService.handle(query)
                .orElseThrow(() -> new AgroSafeException(
                        "User not found with ID: " + userId));
        AdminUserResource resource = AdminUserResourceFromEntityAssembler.toResourceFromEntity(user);
        return ResponseEntity.ok(resource);
    }

    public record BatchRegisterRequest(List<BatchDeviceEntry> devices) {
    }

    public record BatchDeviceEntry(String serialNumber, String type) {
    }
}
