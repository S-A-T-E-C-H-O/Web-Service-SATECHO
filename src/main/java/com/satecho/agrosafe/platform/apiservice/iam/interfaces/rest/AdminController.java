package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.PlatformMetricsResource;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.DeviceResource;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.FarmCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.AdminFarmResource;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Platform-wide administration: farm oversight, fleet-wide device visibility, and
 * aggregate metrics (EP-011-US002/US003). All endpoints require ROLE_ADMIN.
 */
@RestController
@RequestMapping(value = "/api/v1/admin", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Admin", description = "Platform administration endpoints")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final FarmQueryService farmQueryService;
    private final FarmCommandService farmCommandService;
    private final DeviceQueryService deviceQueryService;
    private final UserQueryService userQueryService;

    public AdminController(FarmQueryService farmQueryService, FarmCommandService farmCommandService,
                            DeviceQueryService deviceQueryService, UserQueryService userQueryService) {
        this.farmQueryService = farmQueryService;
        this.farmCommandService = farmCommandService;
        this.deviceQueryService = deviceQueryService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/farms")
    public ResponseEntity<List<AdminFarmResource>> getAllFarms() {
        var farms = farmQueryService.findAll().stream()
                .map(f -> new AdminFarmResource(f.getId(), f.getUserId(), f.getName(), f.getLocation(),
                        f.getHectares(), f.getCropType() != null ? f.getCropType().name() : null, f.isActive()))
                .toList();
        return ResponseEntity.ok(farms);
    }

    @PostMapping("/farms/{farmId}/deactivate")
    public ResponseEntity<?> deactivateFarm(@PathVariable Long farmId) {
        var result = farmCommandService.deactivateFarm(farmId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                f -> new AdminFarmResource(f.getId(), f.getUserId(), f.getName(), f.getLocation(), f.getHectares(),
                        f.getCropType() != null ? f.getCropType().name() : null, f.isActive()), HttpStatus.OK);
    }

    @PostMapping("/farms/{farmId}/reactivate")
    public ResponseEntity<?> reactivateFarm(@PathVariable Long farmId) {
        var result = farmCommandService.reactivateFarm(farmId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                f -> new AdminFarmResource(f.getId(), f.getUserId(), f.getName(), f.getLocation(), f.getHectares(),
                        f.getCropType() != null ? f.getCropType().name() : null, f.isActive()), HttpStatus.OK);
    }

    @GetMapping("/devices")
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var devices = deviceQueryService.findAll().stream().map(DeviceResourceFromEntityAssembler::toResource).toList();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/metrics")
    public ResponseEntity<PlatformMetricsResource> getMetrics() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        long farmerCount = users.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_FARMER)).count();
        long agronomistCount = users.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_AGRONOMIST)).count();
        long adminCount = users.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_ADMIN)).count();
        long blockedCount = users.stream().filter(u -> Boolean.TRUE.equals(u.getBlocked())).count();

        var farms = farmQueryService.findAll();
        long activeFarms = farms.stream().filter(f -> f.isActive()).count();

        var devices = deviceQueryService.findAll();
        long onlineDevices = devices.stream().filter(d -> Boolean.TRUE.equals(d.getOnline())).count();

        var metrics = new PlatformMetricsResource(users.size(), farmerCount, agronomistCount, adminCount,
                blockedCount, farms.size(), activeFarms, devices.size(), onlineDevices);
        return ResponseEntity.ok(metrics);
    }
}
