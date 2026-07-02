package com.satecho.agrosafe.platform.apiservice.analytics.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.DashboardQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.ParcelComparisonQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.interfaces.rest.resources.FarmerDashboardResource;
import com.satecho.agrosafe.platform.apiservice.analytics.interfaces.rest.resources.ParcelComparisonResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.ResourceOwnershipService;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Analytics", description = "Farmer dashboard and parcel comparison (EP-013)")
public class AnalyticsController {

    private final DashboardQueryService dashboardQueryService;
    private final ParcelComparisonQueryService parcelComparisonQueryService;
    private final ResourceOwnershipService resourceOwnershipService;

    public AnalyticsController(DashboardQueryService dashboardQueryService, ParcelComparisonQueryService parcelComparisonQueryService,
                                ResourceOwnershipService resourceOwnershipService) {
        this.dashboardQueryService = dashboardQueryService;
        this.parcelComparisonQueryService = parcelComparisonQueryService;
        this.resourceOwnershipService = resourceOwnershipService;
    }

    @GetMapping("/dashboard/farmer")
    public ResponseEntity<FarmerDashboardResource> getFarmerDashboard() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var dashboard = dashboardQueryService.getFarmerDashboard(userId);
        var farms = dashboard.farms().stream()
                .map(f -> new FarmerDashboardResource.FarmResource(f.id(), f.name(), f.location(), f.cropType(), f.hectares()))
                .toList();
        return ResponseEntity.ok(new FarmerDashboardResource(dashboard.totalZones(), dashboard.onlineDevices(),
                dashboard.offlineDevices(), dashboard.errorDevices(), farms));
    }

    @GetMapping("/analytics/parcels/compare")
    public ResponseEntity<?> compareParcels(@RequestParam List<Long> zoneIds) {
        for (Long zoneId : zoneIds) {
            if (!resourceOwnershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        }
        var result = parcelComparisonQueryService.compare(zoneIds);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                list -> list.stream().map(c -> new ParcelComparisonResource(c.zoneId(), c.zoneName(), c.soilMoisture(),
                        c.soilTemperature(), c.electricalConductivity(), c.areaHectares(), c.cropType())).toList(),
                HttpStatus.OK);
    }
}
