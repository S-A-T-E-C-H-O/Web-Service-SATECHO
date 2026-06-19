package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.ZoneCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.exceptions.ZoneNotFoundException;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.LinkDeviceToZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.LinkDeviceResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.UpdateZoneResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.UpdateZoneThresholdResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.ZoneResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform.UpdateZoneThresholdCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform.ZoneResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/zones", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Zones", description = "Irrigation zone management endpoints")
@PreAuthorize("isAuthenticated()")
public class ZoneController {

    private final ZoneCommandService zoneCommandService;
    private final ZoneQueryService zoneQueryService;

    public ZoneController(ZoneCommandService zoneCommandService, ZoneQueryService zoneQueryService) {
        this.zoneCommandService = zoneCommandService;
        this.zoneQueryService = zoneQueryService;
    }

    @GetMapping("/{zoneId}")
    public ResponseEntity<ZoneResource> getZoneById(@PathVariable Long zoneId) {
        var zone = zoneQueryService.findById(zoneId)
                .orElseThrow(() -> new ZoneNotFoundException(zoneId));
        return ResponseEntity.ok(ZoneResourceFromEntityAssembler.toResourceFromEntity(zone));
    }

    @PatchMapping("/{zoneId}")
    public ResponseEntity<?> updateZone(@PathVariable Long zoneId, @RequestBody UpdateZoneResource resource) {
        var result = zoneCommandService.updateZone(zoneId, resource.name(), resource.areaHectares());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ZoneResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @PatchMapping("/{zoneId}/thresholds")
    public ResponseEntity<?> updateThresholds(@PathVariable Long zoneId, @RequestBody UpdateZoneThresholdResource resource) {
        var command = UpdateZoneThresholdCommandFromResourceAssembler.toCommandFromResource(resource, zoneId);
        var result = zoneCommandService.updateThresholds(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ZoneResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @PostMapping("/{zoneId}/devices")
    public ResponseEntity<?> linkDevice(@PathVariable Long zoneId, @RequestBody LinkDeviceResource resource) {
        var command = new LinkDeviceToZoneCommand(zoneId, resource.deviceId());
        var result = zoneCommandService.linkDevice(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ZoneResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }
}