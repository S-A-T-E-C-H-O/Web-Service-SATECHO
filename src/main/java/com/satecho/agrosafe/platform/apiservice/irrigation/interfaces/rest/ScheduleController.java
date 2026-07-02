package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices.ScheduleCommandService;
import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.ScheduleQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.CreateScheduleCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.DeleteScheduleCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.UpdateScheduleCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetScheduleByIdQuery;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetSchedulesByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.RecurrencePattern;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.CreateScheduleResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.IrrigationScheduleResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.UpdateScheduleResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform.IrrigationScheduleResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.ResourceOwnershipService;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/zones/{zoneId}/irrigation/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Irrigation Schedules", description = "Irrigation schedule management endpoints")
@PreAuthorize("isAuthenticated()")
public class ScheduleController {

    private final ScheduleCommandService scheduleCommandService;
    private final ScheduleQueryService scheduleQueryService;
    private final ResourceOwnershipService ownershipService;

    public ScheduleController(ScheduleCommandService scheduleCommandService, ScheduleQueryService scheduleQueryService,
                               ResourceOwnershipService ownershipService) {
        this.scheduleCommandService = scheduleCommandService;
        this.scheduleQueryService = scheduleQueryService;
        this.ownershipService = ownershipService;
    }

    @PostMapping
    public ResponseEntity<?> createSchedule(@PathVariable Long zoneId, @RequestBody CreateScheduleResource resource) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var command = new CreateScheduleCommand(zoneId, resource.deviceId(), resource.startAt(),
                resource.durationMinutes(), RecurrencePattern.valueOf(resource.recurrence()),
                resource.cronExpression(), resource.enabled());
        var result = scheduleCommandService.createSchedule(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, IrrigationScheduleResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @GetMapping
    public ResponseEntity<List<IrrigationScheduleResource>> listSchedules(@PathVariable Long zoneId) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var query = new GetSchedulesByZoneQuery(zoneId);
        var schedules = scheduleQueryService.handle(query);
        var resources = schedules.stream()
                .map(IrrigationScheduleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long zoneId, @PathVariable Long scheduleId,
                                            @RequestBody UpdateScheduleResource resource) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var command = new UpdateScheduleCommand(scheduleId, zoneId, resource.startAt(),
                resource.durationMinutes(), RecurrencePattern.valueOf(resource.recurrence()),
                resource.cronExpression(), resource.enabled());
        var result = scheduleCommandService.updateSchedule(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, IrrigationScheduleResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<?> deleteSchedule(@PathVariable Long zoneId, @PathVariable Long scheduleId) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var command = new DeleteScheduleCommand(scheduleId, zoneId);
        var result = scheduleCommandService.deleteSchedule(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, v -> v, ResponseEntity.ok().build().getStatusCode());
    }
}
