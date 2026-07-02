package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AlertQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.AlertResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.AlertResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Advisory Alerts", description = "Soil alert endpoints (moisture, salinity, temperature)")
public class AlertController {

    private final AlertQueryService alertQueryService;
    private final ZoneQueryService zoneQueryService;
    private final FarmQueryService farmQueryService;

    public AlertController(AlertQueryService alertQueryService, ZoneQueryService zoneQueryService,
                            FarmQueryService farmQueryService) {
        this.alertQueryService = alertQueryService;
        this.zoneQueryService = zoneQueryService;
        this.farmQueryService = farmQueryService;
    }

    @GetMapping("/zones/{zoneId}/alerts")
    public ResponseEntity<List<AlertResource>> getAlertsByZone(@PathVariable Long zoneId) {
        var zone = zoneQueryService.findById(zoneId);
        if (zone.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(zone.get().getFarmId())) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceList(alertQueryService.findByZoneId(zoneId)));
    }

    @GetMapping("/farms/{farmId}/alerts/active")
    public ResponseEntity<List<AlertResource>> getActiveAlertsByFarm(@PathVariable Long farmId) {
        if (!isOwnerOrAdmin(farmId)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceList(alertQueryService.findActiveByFarmId(farmId)));
    }

    private boolean isOwnerOrAdmin(Long farmId) {
        if (SecurityContextUtil.isAdmin()) return true;
        return farmQueryService.findById(farmId)
                .map(farm -> farm.getUserId().equals(SecurityContextUtil.getCurrentUserId()))
                .orElse(false);
    }
}
