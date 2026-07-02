package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.PlatformMetricsResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.RegistrationsTrendPointResource;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.DeviceResource;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform.DeviceResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.FarmCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.AdminFarmResource;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

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
    private final TelemetryQueryService telemetryQueryService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final PlanQueryService planQueryService;

    public AdminController(FarmQueryService farmQueryService, FarmCommandService farmCommandService,
                            DeviceQueryService deviceQueryService, UserQueryService userQueryService,
                            TelemetryQueryService telemetryQueryService, SubscriptionQueryService subscriptionQueryService,
                            PlanQueryService planQueryService) {
        this.farmQueryService = farmQueryService;
        this.farmCommandService = farmCommandService;
        this.deviceQueryService = deviceQueryService;
        this.userQueryService = userQueryService;
        this.telemetryQueryService = telemetryQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.planQueryService = planQueryService;
    }

    @GetMapping("/farms")
    public ResponseEntity<List<AdminFarmResource>> getAllFarms() {
        var farms = farmQueryService.findAll().stream().map(this::toFarmResource).toList();
        return ResponseEntity.ok(farms);
    }

    @PostMapping("/farms/{farmId}/deactivate")
    public ResponseEntity<?> deactivateFarm(@PathVariable Long farmId) {
        var result = farmCommandService.deactivateFarm(farmId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toFarmResource, HttpStatus.OK);
    }

    @PostMapping("/farms/{farmId}/reactivate")
    public ResponseEntity<?> reactivateFarm(@PathVariable Long farmId) {
        var result = farmCommandService.reactivateFarm(farmId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toFarmResource, HttpStatus.OK);
    }

    private AdminFarmResource toFarmResource(Farm f) {
        var owner = userQueryService.handle(new GetUserByIdQuery(f.getUserId()));
        var planTier = subscriptionQueryService.findByUserId(f.getUserId())
                .flatMap(s -> planQueryService.findById(s.getPlanId()))
                .map(p -> p.getTier().name())
                .orElse(null);
        long activeDeviceCount = deviceQueryService.findAll().stream()
                .filter(d -> d.getUserId().equals(f.getUserId()) && d.getOnline())
                .count();
        return new AdminFarmResource(f.getId(), f.getUserId(), f.getName(), f.getLocation(), f.getHectares(),
                f.getCropType() != null ? f.getCropType().name() : null, f.isActive(),
                owner.map(u -> u.getEmail()).orElse(null), planTier, activeDeviceCount);
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

        long readingsLast5Min = telemetryQueryService.countReadingsSince(Instant.now().minus(5, ChronoUnit.MINUTES));
        double readingsPerMinute = readingsLast5Min / 5.0;

        var metrics = new PlatformMetricsResource(users.size(), farmerCount, agronomistCount, adminCount,
                blockedCount, farms.size(), activeFarms, devices.size(), onlineDevices, readingsPerMinute);
        return ResponseEntity.ok(metrics);
    }

    /** EP-011-US003 Scenario 2: 30-day daily registration counts, split by role. */
    @GetMapping("/metrics/registrations-trend")
    public ResponseEntity<List<RegistrationsTrendPointResource>> getRegistrationsTrend() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        var since = LocalDate.now(ZoneOffset.UTC).minusDays(29);

        var byDay = users.stream()
                .filter(u -> u.getCreatedAt() != null)
                .collect(Collectors.groupingBy(u -> u.getCreatedAt().toInstant().atZone(ZoneOffset.UTC).toLocalDate()));

        var trend = since.datesUntil(LocalDate.now(ZoneOffset.UTC).plusDays(1))
                .map(day -> {
                    var dayUsers = byDay.getOrDefault(day, List.of());
                    long farmers = dayUsers.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_FARMER)).count();
                    long agronomists = dayUsers.stream().filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_AGRONOMIST)).count();
                    return new RegistrationsTrendPointResource(day, farmers, agronomists);
                })
                .toList();

        return ResponseEntity.ok(trend);
    }
}
