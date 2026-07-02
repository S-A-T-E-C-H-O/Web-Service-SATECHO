package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.application.queryservices.SecurityQueryService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.SecurityEventCsvExportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Perimeter Security", description = "Security event endpoints")
public class PerimeterSecurityController {

    private final SecurityCommandService securityCommandService;
    private final SecurityQueryService securityQueryService;
    private final FarmQueryService farmQueryService;
    private final SecurityEventCsvExportService securityEventCsvExportService;

    public PerimeterSecurityController(SecurityCommandService securityCommandService, SecurityQueryService securityQueryService,
                                        FarmQueryService farmQueryService, SecurityEventCsvExportService securityEventCsvExportService) {
        this.securityCommandService = securityCommandService;
        this.securityQueryService = securityQueryService;
        this.farmQueryService = farmQueryService;
        this.securityEventCsvExportService = securityEventCsvExportService;
    }

    // Called by the Edge service on behalf of ESP32 hardware (device credential,
    // not a farmer session) — no per-farmer ownership check applies here.
    @PostMapping("/security/events/ingest")
    public ResponseEntity<?> ingestSecurityEvent(@RequestBody IngestSecurityEventResource resource) {
        var command = IngestSecurityEventCommandFromResourceAssembler.toCommand(resource);
        var result = securityCommandService.ingestSecurityEvent(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SecurityEventResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
    }

    @GetMapping("/farms/{farmId}/security/events")
    public ResponseEntity<List<SecurityEventResource>> getSecurityEventsByFarm(
            @PathVariable Long farmId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to,
            @RequestParam(required = false) String severity,
            @RequestParam(required = false) String classification,
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(defaultValue = "0") Integer page) {
        if (!isOwnerOrAdmin(farmId)) return ResponseEntity.status(403).build();
        EventSeverity eventSeverity = null;
        if (severity != null && !severity.isBlank()) {
            try { eventSeverity = EventSeverity.valueOf(severity.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }
        EventClassification eventClassification = null;
        if (classification != null && !classification.isBlank()) {
            try { eventClassification = EventClassification.valueOf(classification.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }
        var query = new GetSecurityEventsByFarmQuery(farmId, from, to, eventSeverity, eventClassification, limit, page);
        return ResponseEntity.ok(SecurityEventResourceFromEntityAssembler.toResourceList(securityQueryService.handle(query)));
    }

    @GetMapping("/security/events/{eventId}")
    public ResponseEntity<SecurityEventResource> getSecurityEventById(@PathVariable Long eventId) {
        var event = securityQueryService.handle(new GetSecurityEventByIdQuery(eventId));
        if (event.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(event.get().getFarmId())) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(SecurityEventResourceFromEntityAssembler.toResource(event.get()));
    }

    @PatchMapping("/security/events/{eventId}")
    public ResponseEntity<?> acknowledgeSecurityEvent(@PathVariable Long eventId, @RequestBody AcknowledgeEventResource resource) {
        var event = securityQueryService.handle(new GetSecurityEventByIdQuery(eventId));
        if (event.isEmpty()) return ResponseEntity.notFound().build();
        if (!isOwnerOrAdmin(event.get().getFarmId())) return ResponseEntity.status(403).build();
        var command = AcknowledgeSecurityEventCommandFromResourceAssembler.toCommand(eventId, resource);
        var result = securityCommandService.acknowledgeSecurityEvent(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SecurityEventResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }

    @GetMapping(value = "/farms/{farmId}/security/events/export", produces = "text/csv")
    public ResponseEntity<byte[]> exportSecurityEventsCsv(
            @PathVariable Long farmId,
            @RequestParam(required = false) Instant from,
            @RequestParam(required = false) Instant to) {
        if (!isOwnerOrAdmin(farmId)) return ResponseEntity.status(403).build();
        var query = new GetSecurityEventsByFarmQuery(farmId, from, to, null, null, 1000, 0);
        var events = securityQueryService.handle(query);
        var csv = securityEventCsvExportService.toCsv(events);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"security-events-farm-" + farmId + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    private boolean isOwnerOrAdmin(Long farmId) {
        if (SecurityContextUtil.isAdmin()) return true;
        return farmQueryService.findById(farmId)
                .map(farm -> farm.getUserId().equals(SecurityContextUtil.getCurrentUserId()))
                .orElse(false);
    }
}
