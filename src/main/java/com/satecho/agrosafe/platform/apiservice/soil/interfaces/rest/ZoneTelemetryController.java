package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetLatestReadingsByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.SensorReadingResource;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform.SensorReadingResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/zones/{zoneId}/telemetry", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Zone Telemetry", description = "Zone-scoped telemetry endpoints")
@PreAuthorize("isAuthenticated()")
public class ZoneTelemetryController {

    private final TelemetryQueryService telemetryQueryService;

    public ZoneTelemetryController(TelemetryQueryService telemetryQueryService) {
        this.telemetryQueryService = telemetryQueryService;
    }

    @GetMapping("/latest")
    public ResponseEntity<List<SensorReadingResource>> getLatest(
            @PathVariable Long zoneId,
            @RequestParam(required = false) String metricType) {
        var readings = telemetryQueryService.getLatestReadingsByZone(new GetLatestReadingsByZoneQuery(zoneId));
        var result = readings.stream()
                .filter(r -> metricType == null || (r.getMetricType() != null &&
                        r.getMetricType().name().equalsIgnoreCase(metricType)))
                .map(SensorReadingResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(result);
    }
}
