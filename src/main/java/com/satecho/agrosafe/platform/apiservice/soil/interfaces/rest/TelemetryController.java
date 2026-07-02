package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.TelemetryCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.ResourceOwnershipService;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/telemetry", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Telemetry", description = "Sensor telemetry endpoints")
@PreAuthorize("isAuthenticated()")
public class TelemetryController {

    private final TelemetryCommandService telemetryCommandService;
    private final TelemetryQueryService telemetryQueryService;
    private final ResourceOwnershipService ownershipService;

    public TelemetryController(TelemetryCommandService telemetryCommandService, TelemetryQueryService telemetryQueryService,
                                ResourceOwnershipService ownershipService) {
        this.telemetryCommandService = telemetryCommandService;
        this.telemetryQueryService = telemetryQueryService;
        this.ownershipService = ownershipService;
    }

    // Called by the Edge service on behalf of ESP32 hardware — no farmer
    // ownership check applies to ingestion.
    @PostMapping
    public ResponseEntity<?> ingestTelemetry(@RequestBody BatchIngestResource resource) {
        var command = new IngestTelemetryCommand(resource.deviceId(), resource.zoneId(), resource.metricType(), resource.value(), resource.timestamp());
        var result = telemetryCommandService.ingestTelemetry(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SensorReadingResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PostMapping("/batch")
    public ResponseEntity<BatchIngestSummaryResource> batchIngest(@RequestBody List<BatchIngestResource> resources) {
        List<String> errors = new ArrayList<>();
        int ingested = 0, failed = 0;
        var command = BatchIngestCommandFromResourceAssembler.toCommandFromResources(resources);
        for (int i = 0; i < resources.size(); i++) {
            try {
                telemetryCommandService.ingestTelemetry(command.readings().get(i));
                ingested++;
            } catch (Exception e) { failed++; errors.add("Reading[" + i + "]: " + e.getMessage()); }
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new BatchIngestSummaryResource(ingested, failed, errors));
    }

    @GetMapping("/zones/{zoneId}/latest")
    public ResponseEntity<List<SensorReadingResource>> getLatestReadingsByZone(@PathVariable Long zoneId) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var readings = telemetryQueryService.getLatestReadingsByZone(new GetLatestReadingsByZoneQuery(zoneId));
        return ResponseEntity.ok(readings.stream().map(SensorReadingResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/zones/{zoneId}/history")
    public ResponseEntity<List<SensorReadingResource>> getZoneHistory(
            @PathVariable Long zoneId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(required = false) String metric) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        MetricType metricType = metric != null ? MetricType.valueOf(metric.toUpperCase()) : null;
        var readings = telemetryQueryService.getReadingsByZone(new GetReadingsByZoneQuery(zoneId, from, to, metricType));
        return ResponseEntity.ok(readings.stream().map(SensorReadingResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/devices/{deviceId}/latest")
    public ResponseEntity<SensorReadingResource> getLatestReadingByDevice(@PathVariable Long deviceId) {
        if (!ownershipService.isDeviceOwnerOrAdmin(deviceId)) return ResponseEntity.status(403).build();
        var readings = telemetryQueryService.getReadingsByDevice(new GetReadingsByDeviceQuery(deviceId));
        if (readings.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(SensorReadingResourceFromEntityAssembler.toResourceFromEntity(readings.get(0)));
    }

    @GetMapping("/zones/{zoneId}/trends")
    public ResponseEntity<List<ZoneTrendResource>> getZoneTrends(@PathVariable Long zoneId, @RequestParam(defaultValue = "30") int days) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var from = Instant.now().minusSeconds((long) days * 24 * 60 * 60);
        var to = Instant.now();
        List<ZoneTrendResource> trends = new ArrayList<>();
        var ecReadings = telemetryQueryService.getReadingsByTimeRange(new GetReadingsByTimeRangeQuery(zoneId, from, to, MetricType.ELECTRICAL_CONDUCTIVITY));
        if (!ecReadings.isEmpty()) trends.add(ZoneTrendResourceFromEntityAssembler.toResourceFromReadings(MetricType.ELECTRICAL_CONDUCTIVITY, ecReadings));
        var phReadings = telemetryQueryService.getReadingsByTimeRange(new GetReadingsByTimeRangeQuery(zoneId, from, to, MetricType.SOIL_PH));
        if (!phReadings.isEmpty()) trends.add(ZoneTrendResourceFromEntityAssembler.toResourceFromReadings(MetricType.SOIL_PH, phReadings));
        return ResponseEntity.ok(trends);
    }
}
