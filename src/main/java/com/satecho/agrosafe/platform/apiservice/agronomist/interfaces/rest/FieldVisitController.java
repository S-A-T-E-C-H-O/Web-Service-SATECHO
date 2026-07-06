package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.FieldVisitCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.FieldVisitQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CompleteFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.ScheduleFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.CompleteFieldVisitResource;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.FieldVisitResource;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.ScheduleFieldVisitResource;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * Controller to manage field visits for agronomists, allowing scheduling, listing, and completing visits.
 */
@RestController
@RequestMapping(value = "/api/v1/agronomist/visits", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Field Visits", description = "Agronomist field visit agenda (EP-009-US008)")
@PreAuthorize("hasRole('AGRONOMIST')")
public class FieldVisitController {

    /**
     * Query service for field visits.
     */
    private final FieldVisitQueryService fieldVisitQueryService;

    /**
     * Command service for field visits.
     */
    private final FieldVisitCommandService fieldVisitCommandService;

    /**
     * Query service for farms.
     */
    private final FarmQueryService farmQueryService;

    /**
     * Query service for users.
     */
    private final UserQueryService userQueryService;

    /**
     * Constructs a new FieldVisitController.
     *
     * @param fieldVisitQueryService the query service for field visits
     * @param fieldVisitCommandService the command service for field visits
     * @param farmQueryService the query service for farms
     * @param userQueryService the query service for users
     */
    public FieldVisitController(FieldVisitQueryService fieldVisitQueryService, FieldVisitCommandService fieldVisitCommandService,
                                 FarmQueryService farmQueryService, UserQueryService userQueryService) {
        this.fieldVisitQueryService = fieldVisitQueryService;
        this.fieldVisitCommandService = fieldVisitCommandService;
        this.farmQueryService = farmQueryService;
        this.userQueryService = userQueryService;
    }

    /**
     * Retrieves scheduled field visits for the currently logged-in agronomist.
     *
     * @return a response entity containing a list of field visit resources
     */
    @GetMapping
    public ResponseEntity<List<FieldVisitResource>> getScheduledVisits() {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var visits = fieldVisitQueryService.findByAgronomistUserId(agronomistUserId).stream()
                .map(this::toResource).toList();
        return ResponseEntity.ok(visits);
    }

    /**
     * Schedules a new field visit.
     *
     * @param resource the resource representing the field visit details to schedule
     * @return a response entity representing the scheduled field visit status
     */
    @PostMapping
    public ResponseEntity<?> scheduleVisit(@RequestBody ScheduleFieldVisitResource resource) {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        Instant scheduledAt = resource.scheduledAt() != null ? Instant.parse(resource.scheduledAt()) : null;
        var command = new ScheduleFieldVisitCommand(agronomistUserId, resource.farmId(), scheduledAt,
                resource.tag(), resource.noteTitle(), resource.noteBody(), resource.urgent(),
                resource.latitude(), resource.longitude(), resource.photoBase64());
        var result = fieldVisitCommandService.scheduleVisit(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.CREATED);
    }

    /**
     * Completes an existing field visit with GPS coordinates and proof photo.
     *
     * @param visitId the unique identifier of the field visit
     * @param resource the resource representing completion data (optional)
     * @return a response entity containing the updated field visit resource
     */
    @PostMapping("/{visitId}/complete")
    public ResponseEntity<?> completeVisit(@PathVariable Long visitId,
                                            @RequestBody(required = false) CompleteFieldVisitResource resource) {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var body = resource != null ? resource : new CompleteFieldVisitResource(null, null, null);
        var command = new CompleteFieldVisitCommand(visitId, agronomistUserId, body.latitude(), body.longitude(),
                body.photoBase64());
        var result = fieldVisitCommandService.completeVisit(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.OK);
    }

    /**
     * Converts a FieldVisit entity into its corresponding FieldVisitResource representation.
     *
     * @param v the field visit entity
     * @return the field visit resource
     */
    private FieldVisitResource toResource(FieldVisit v) {
        var farm = farmQueryService.findById(v.getFarmId()).orElse(null);
        String farmName = farm != null ? farm.getName() : null;
        String ownerName = farm != null
                ? userQueryService.handle(new GetUserByIdQuery(farm.getUserId())).map(u -> u.getFullName()).orElse(null)
                : null;
        return new FieldVisitResource(v.getId(), v.getFarmId(), farmName, ownerName,
                v.getScheduledAt() != null ? v.getScheduledAt().toString() : null,
                v.getTag(), v.getNoteTitle(), v.getNoteBody(), v.getUrgent(), v.getCompleted(),
                v.getLatitude(), v.getLongitude(), v.getPhotoBase64());
    }
}

