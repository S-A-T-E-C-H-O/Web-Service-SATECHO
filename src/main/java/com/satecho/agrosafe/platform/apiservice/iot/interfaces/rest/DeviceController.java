package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iot.application.commandservices.DeviceCommandService;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.resources.MessageResource;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Devices", description = "Device management endpoints")
public class DeviceController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DeviceController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @PostMapping
    public ResponseEntity<?> registerDevice(@Valid @RequestBody RegisterDeviceResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = RegisterDeviceCommandFromResourceAssembler.toCommand(resource, userId);
        var result = deviceCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, DeviceResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DeviceResource>> getAllDevices(
            @RequestParam(required = false) DeviceType type,
            @RequestParam(required = false) DeviceStatus status) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var query = new GetAllDevicesByUserIdQuery(userId, type, status);
        var devices = deviceQueryService.handle(query);
        var resources = devices.stream().map(DeviceResourceFromEntityAssembler::toResource).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        return device.map(d -> ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResource(d)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{deviceId}/activate")
    public ResponseEntity<?> activateDevice(@PathVariable Long deviceId, @Valid @RequestBody ActivateDeviceResource resource) {
        var command = ActivateDeviceCommandFromResourceAssembler.toCommand(resource, deviceId);
        var result = deviceCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, DeviceResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @PostMapping("/{deviceId}/deactivate")
    public ResponseEntity<?> deactivateDevice(@PathVariable Long deviceId) {
        var result = deviceCommandService.handleDeactivate(deviceId);
        if (result.isFailure()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new MessageResource("Device deactivated successfully"));
    }

    @PostMapping("/{deviceId}/decommission")
    public ResponseEntity<?> decommissionDevice(@PathVariable Long deviceId) {
        var result = deviceCommandService.handleDecommission(deviceId);
        if (result.isFailure()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new MessageResource("Device decommissioned successfully"));
    }

    @PostMapping("/{deviceId}/firmware")
    public ResponseEntity<?> updateFirmware(@PathVariable Long deviceId, @Valid @RequestBody UpdateFirmwareResource resource) {
        var command = new UpdateFirmwareCommand(deviceId, resource.firmwareVersion());
        var result = deviceCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, DeviceResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping("/{deviceId}/status")
    public ResponseEntity<DeviceStatusResource> getDeviceStatus(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        var d = device.get();
        return ResponseEntity.ok(new DeviceStatusResource(d.getId(), d.getSerialNumber(), d.getOnline(),
                d.getLastHeartbeatAt(), d.getLastTelemetryAt(),
                d.getBatteryLevel(), d.getHealthStatus(), d.getStatus().name(), d.getFirmwareVersion()));
    }

    @PostMapping("/{deviceId}/heartbeat")
    public ResponseEntity<?> heartbeat(@PathVariable Long deviceId, @RequestBody HeartbeatResource resource) {
        var result = deviceCommandService.recordHeartbeat(deviceId, resource.batteryLevel());
        return ResponseEntityAssembler.toResponseEntityFromResult(result, DeviceResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping("/{deviceId}/diagnostics")
    public ResponseEntity<DeviceDiagnosticResource> getDiagnostics(@PathVariable Long deviceId) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(deviceId));
        if (device.isEmpty()) return ResponseEntity.notFound().build();
        var d = device.get();
        var history = List.of(
                new DeviceDiagnosticResource.ConnectionHistoryEntry(d.getLastHeartbeatAt(), "HEARTBEAT", "Device sent heartbeat"),
                new DeviceDiagnosticResource.ConnectionHistoryEntry(d.getLastTelemetryAt(), "TELEMETRY", "Device sent telemetry data"));
        double uptimeHours = d.getLastHeartbeatAt() != null
                ? Duration.between(d.getLastHeartbeatAt(), Instant.now()).toSeconds() / 3600.0 : 0;
        return ResponseEntity.ok(new DeviceDiagnosticResource(d.getId(), d.getSerialNumber(), d.getType().name(),
                d.getStatus().name(), d.getFirmwareVersion(), d.getBatteryLevel(), d.getHealthStatus(),
                d.getOnline(), d.isWithinHeartbeatWindow() ? "EXCELLENT" : "POOR", uptimeHours,
                d.getLastHeartbeatAt(), history));
    }

    @PostMapping("/batch")
    public ResponseEntity<?> batchRegister(@Valid @RequestBody BatchRegisterResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var result = deviceCommandService.handleBatchRegister(userId, resource.devices());
        if (result.isSuccess()) {
            var r = result.toOptional().orElseThrow();
            var entries = r.entries().stream()
                    .map(e -> new BatchRegisterSummaryResource.BatchEntryResource(e.serialNumber(), e.status(), e.deviceId(), e.error()))
                    .toList();
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BatchRegisterSummaryResource(r.totalSubmitted(), r.succeeded(), r.failed(), entries));
        }
        return ResponseEntity.badRequest().build();
    }
}
