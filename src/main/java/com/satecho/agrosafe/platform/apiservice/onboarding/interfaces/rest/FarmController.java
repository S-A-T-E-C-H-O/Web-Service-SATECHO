package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.FarmCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.ZoneCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/farms", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Farms", description = "Farm management endpoints")
@PreAuthorize("isAuthenticated()")
public class FarmController {

    private final FarmCommandService farmCommandService;
    private final FarmQueryService farmQueryService;
    private final ZoneCommandService zoneCommandService;
    private final ZoneQueryService zoneQueryService;

    public FarmController(FarmCommandService farmCommandService, FarmQueryService farmQueryService,
                          ZoneCommandService zoneCommandService, ZoneQueryService zoneQueryService) {
        this.farmCommandService = farmCommandService;
        this.farmQueryService = farmQueryService;
        this.zoneCommandService = zoneCommandService;
        this.zoneQueryService = zoneQueryService;
    }

    @PostMapping
    public ResponseEntity<?> createFarm(@RequestBody CreateFarmResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = CreateFarmCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var result = farmCommandService.createFarm(command);

        if (result.isSuccess() && resource.zones() != null) {
            var farm = result.toOptional().orElseThrow();
            for (var zoneRes : resource.zones()) {
                var zoneCommand = CreateZoneCommandFromResourceAssembler.toCommandFromResource(zoneRes, farm.getId());
                zoneCommandService.createZone(zoneCommand);
            }
        }

        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, FarmResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FarmResource>> getAllFarms() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var farms = farmQueryService.findAllByUserId(userId);
        var resources = farms.stream().map(FarmResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{farmId}")
    public ResponseEntity<FarmResource> getFarmById(@PathVariable Long farmId) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(farm.get().getUserId())) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(FarmResourceFromEntityAssembler.toResourceFromEntity(farm.get()));
    }

    @PutMapping("/{farmId}")
    public ResponseEntity<?> updateFarm(@PathVariable Long farmId, @RequestBody UpdateFarmResource resource) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(farm.get().getUserId())) return ResponseEntity.status(403).build();
        var command = UpdateFarmCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = farmCommandService.updateFarm(farmId, command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, FarmResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @DeleteMapping("/{farmId}")
    public ResponseEntity<?> deleteFarm(@PathVariable Long farmId) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(farm.get().getUserId())) return ResponseEntity.status(403).build();
        var result = farmCommandService.deleteFarm(farmId);
        if (result.isFailure()) {
            return ResponseEntityAssembler.toResponseEntityFromResult(
                    result, v -> null, HttpStatus.OK);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{farmId}/zones")
    public ResponseEntity<?> createZone(@PathVariable Long farmId, @RequestBody CreateZoneResource resource) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(farm.get().getUserId())) return ResponseEntity.status(403).build();
        var command = CreateZoneCommandFromResourceAssembler.toCommandFromResource(resource, farmId);
        var result = zoneCommandService.createZone(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, ZoneResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping("/{farmId}/zones")
    public ResponseEntity<List<ZoneResource>> getZonesByFarm(@PathVariable Long farmId) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(farm.get().getUserId())) return ResponseEntity.status(403).build();
        var zones = zoneQueryService.findAllByFarmId(farmId);
        var resources = zones.stream().map(ZoneResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Ownership check: the caller must own the farm, or hold ROLE_ADMIN.
     * Agronomist-client access (viewing an assigned farmer's farm) will extend
     * this once the agronomist bounded context ships (Fase 2).
     */
    private boolean isOwnerOrAdmin(Long farmOwnerUserId) {
        if (SecurityContextUtil.isAdmin()) return true;
        return farmOwnerUserId.equals(SecurityContextUtil.getCurrentUserId());
    }
}