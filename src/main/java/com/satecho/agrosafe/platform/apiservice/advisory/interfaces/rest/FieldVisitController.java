package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.FieldVisitCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.FieldVisitQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.CompleteVisitCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.ScheduleVisitCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries.GetVisitsByAgronomistQuery;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ScheduleVisitResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ScheduledVisitResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.ScheduledVisitResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Field Visits", description = "Field visit scheduling endpoints")
@PreAuthorize("isAuthenticated()")
public class FieldVisitController {

    private final FieldVisitCommandService commandService;
    private final FieldVisitQueryService queryService;
    private final FarmQueryService farmQueryService;
    private final UserQueryService userQueryService;

    public FieldVisitController(FieldVisitCommandService commandService,
                                FieldVisitQueryService queryService,
                                FarmQueryService farmQueryService,
                                UserQueryService userQueryService) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.farmQueryService = farmQueryService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/agronomist/visits")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<List<ScheduledVisitResource>> getVisits() {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var visits = queryService.handle(new GetVisitsByAgronomistQuery(agronomistId));
        var resources = visits.stream().map(visit -> {
            Farm farm = farmQueryService.findById(visit.getFarmId()).orElse(null);
            var owner = farm != null
                    ? userQueryService.handle(new GetUserByIdQuery(farm.getUserId())).orElse(null)
                    : null;
            return ScheduledVisitResourceAssembler.toResource(visit, farm, owner);
        }).toList();
        return ResponseEntity.ok(resources);
    }

    @PostMapping("/agronomist/visits")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<ScheduledVisitResource> scheduleVisit(@RequestBody ScheduleVisitResource resource) {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var command = new ScheduleVisitCommand(agronomistId, resource.farmId(), resource.scheduledAt(),
                resource.tag(), resource.noteTitle(), resource.noteBody(), resource.urgent());
        var visit = commandService.schedule(command);
        Farm farm = farmQueryService.findById(visit.getFarmId()).orElse(null);
        var owner = farm != null
                ? userQueryService.handle(new GetUserByIdQuery(farm.getUserId())).orElse(null)
                : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ScheduledVisitResourceAssembler.toResource(visit, farm, owner));
    }

    @PostMapping("/agronomist/visits/{visitId}/complete")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<ScheduledVisitResource> completeVisit(@PathVariable Long visitId) {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var visit = commandService.complete(new CompleteVisitCommand(visitId, agronomistId));
        Farm farm = farmQueryService.findById(visit.getFarmId()).orElse(null);
        var owner = farm != null
                ? userQueryService.handle(new GetUserByIdQuery(farm.getUserId())).orElse(null)
                : null;
        return ResponseEntity.ok(ScheduledVisitResourceAssembler.toResource(visit, farm, owner));
    }
}
