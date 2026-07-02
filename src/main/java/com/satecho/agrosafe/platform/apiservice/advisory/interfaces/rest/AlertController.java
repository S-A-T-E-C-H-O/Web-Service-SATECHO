package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AlertQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.AlertResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.AlertResourceFromEntityAssembler;
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

    public AlertController(AlertQueryService alertQueryService) {
        this.alertQueryService = alertQueryService;
    }

    @GetMapping("/zones/{zoneId}/alerts")
    public ResponseEntity<List<AlertResource>> getAlertsByZone(@PathVariable Long zoneId) {
        SecurityContextUtil.getCurrentUserId();
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceList(alertQueryService.findByZoneId(zoneId)));
    }

    @GetMapping("/farms/{farmId}/alerts/active")
    public ResponseEntity<List<AlertResource>> getActiveAlertsByFarm(@PathVariable Long farmId) {
        SecurityContextUtil.getCurrentUserId();
        return ResponseEntity.ok(AlertResourceFromEntityAssembler.toResourceList(alertQueryService.findActiveByFarmId(farmId)));
    }
}
