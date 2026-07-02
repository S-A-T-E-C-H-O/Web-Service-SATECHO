package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices.IrrigationCommandService;
import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.IrrigationQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.StopIrrigationCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetActiveSessionByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetSessionHistoryByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.IrrigationSessionResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.StartIrrigationResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.StopIrrigationResource;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform.IrrigationSessionResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform.StartIrrigationCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.WaterConsumptionReportService;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.ResourceOwnershipService;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/zones/{zoneId}/irrigation", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Irrigation", description = "Irrigation session management endpoints")
@PreAuthorize("isAuthenticated()")
public class IrrigationController {

    private final IrrigationCommandService irrigationCommandService;
    private final IrrigationQueryService irrigationQueryService;
    private final ResourceOwnershipService ownershipService;
    private final WaterConsumptionReportService waterConsumptionReportService;

    public IrrigationController(IrrigationCommandService irrigationCommandService, IrrigationQueryService irrigationQueryService,
                                 ResourceOwnershipService ownershipService, WaterConsumptionReportService waterConsumptionReportService) {
        this.irrigationCommandService = irrigationCommandService;
        this.irrigationQueryService = irrigationQueryService;
        this.ownershipService = ownershipService;
        this.waterConsumptionReportService = waterConsumptionReportService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startIrrigation(@PathVariable Long zoneId, @RequestBody StartIrrigationResource resource) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var command = StartIrrigationCommandFromResourceAssembler.toCommandFromResource(resource, zoneId);
        var result = irrigationCommandService.startIrrigation(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, IrrigationSessionResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @PostMapping("/stop")
    public ResponseEntity<?> stopIrrigation(@PathVariable Long zoneId, @RequestBody StopIrrigationResource resource) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var currentUserId = SecurityContextUtil.getCurrentUserId();
        var command = new StopIrrigationCommand(zoneId, String.valueOf(currentUserId), resource.flowRateLitersPerMinute());
        var result = irrigationCommandService.stopIrrigation(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, IrrigationSessionResourceFromEntityAssembler::toResourceFromEntity, ResponseEntity.ok().build().getStatusCode());
    }

    @GetMapping("/active")
    public ResponseEntity<IrrigationSessionResource> getActiveSession(@PathVariable Long zoneId) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var query = new GetActiveSessionByZoneQuery(zoneId);
        return irrigationQueryService.handle(query)
                .map(IrrigationSessionResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/history")
    public ResponseEntity<List<IrrigationSessionResource>> getSessionHistory(
            @PathVariable Long zoneId,
            @RequestParam(required = false) Instant fromDate,
            @RequestParam(required = false) Instant toDate,
            @RequestParam(required = false, defaultValue = "20") Integer limit) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var query = new GetSessionHistoryByZoneQuery(zoneId, fromDate, toDate, limit);
        var sessions = irrigationQueryService.handle(query);
        var resources = sessions.stream()
                .map(IrrigationSessionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/reports/water-consumption", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> getWaterConsumptionReport(
            @PathVariable Long zoneId,
            @RequestParam Instant fromDate,
            @RequestParam Instant toDate) {
        if (!ownershipService.isZoneOwnerOrAdmin(zoneId)) return ResponseEntity.status(403).build();
        var pdf = waterConsumptionReportService.generatePdf(zoneId, fromDate, toDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"water-consumption-zone-" + zoneId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
