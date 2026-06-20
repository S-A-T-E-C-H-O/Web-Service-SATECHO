package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.application.queryservices.SecurityQueryService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform.*;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    public PerimeterSecurityController(SecurityCommandService securityCommandService, SecurityQueryService securityQueryService) {
        this.securityCommandService = securityCommandService;
        this.securityQueryService = securityQueryService;
    }

    @PostMapping("/security/events/ingest")
    public ResponseEntity<?> ingestSecurityEvent(@RequestBody IngestSecurityEventResource resource) {
        SecurityContextUtil.getCurrentUserId();
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
        SecurityContextUtil.getCurrentUserId();
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
        SecurityContextUtil.getCurrentUserId();
        var event = securityQueryService.handle(new GetSecurityEventByIdQuery(eventId));
        return event.map(e -> ResponseEntity.ok(SecurityEventResourceFromEntityAssembler.toResource(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/security/events/{eventId}")
    public ResponseEntity<?> acknowledgeSecurityEvent(@PathVariable Long eventId, @RequestBody AcknowledgeEventResource resource) {
        SecurityContextUtil.getCurrentUserId();
        var command = AcknowledgeSecurityEventCommandFromResourceAssembler.toCommand(eventId, resource);
        var result = securityCommandService.acknowledgeSecurityEvent(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, SecurityEventResourceFromEntityAssembler::toResource, HttpStatus.OK);
    }
}
