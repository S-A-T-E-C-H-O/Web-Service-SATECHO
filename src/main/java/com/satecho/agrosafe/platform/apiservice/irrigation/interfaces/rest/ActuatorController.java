package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest;

import com.satecho.agrosafe.platform.irrigation.application.commandservices.ActuatorCommandService;
import com.satecho.agrosafe.platform.irrigation.application.queryservices.ActuatorQueryService;
import com.satecho.agrosafe.platform.irrigation.domain.model.commands.LogActuatorCommand;
import com.satecho.agrosafe.platform.irrigation.domain.model.queries.GetActuatorLogsByDeviceQuery;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorType;
import com.satecho.agrosafe.platform.irrigation.interfaces.rest.resources.ActuatorCommandResource;
import com.satecho.agrosafe.platform.irrigation.interfaces.rest.resources.ActuatorLogResource;
import com.satecho.agrosafe.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/actuators/{deviceId}", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Actuators", description = "Actuator command and log endpoints")
@PreAuthorize("isAuthenticated()")
public class ActuatorController {

    private final ActuatorCommandService actuatorCommandService;
    private final ActuatorQueryService actuatorQueryService;

    public ActuatorController(ActuatorCommandService actuatorCommandService, ActuatorQueryService actuatorQueryService) {
        this.actuatorCommandService = actuatorCommandService;
        this.actuatorQueryService = actuatorQueryService;
    }

    @PostMapping("/command")
    public ResponseEntity<?> logCommand(@PathVariable Long deviceId, @RequestBody ActuatorCommandResource resource) {
        var command = new LogActuatorCommand(deviceId, resource.zoneId(),
                ActuatorType.valueOf(resource.actuatorType()), ActuatorAction.valueOf(resource.action()),
                resource.commandSource(), resource.success(), resource.responseMessage());
        var result = actuatorCommandService.logActuatorAction(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ActuatorController::toActuatorLogResource, ResponseEntity.ok().build().getStatusCode());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<ActuatorLogResource>> getLogs(
            @PathVariable Long deviceId,
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        var query = new GetActuatorLogsByDeviceQuery(deviceId, fromDate, toDate, limit);
        var logs = actuatorQueryService.handle(query);
        var resources = logs.stream()
                .map(ActuatorController::toActuatorLogResource)
                .toList();
        return ResponseEntity.ok(resources);
    }

    private static ActuatorLogResource toActuatorLogResource(com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.ActuatorLog log) {
        return new ActuatorLogResource(
                log.getId(), log.getDeviceId(), log.getZoneId(),
                log.getActuatorType().name(), log.getAction().name(),
                log.getCommandSource(), log.getExecutedAt(),
                log.isSuccess(), log.getResponseMessage());
    }
}
