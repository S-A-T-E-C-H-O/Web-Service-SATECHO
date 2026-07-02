package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.RecommendationCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.RecommendationQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CreateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.CreateRecommendationResource;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.RecommendationResource;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/recommendations", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recommendations", description = "Agronomist recommendations to farmers (EP-009-US004)")
@PreAuthorize("isAuthenticated()")
public class RecommendationController {

    private final RecommendationQueryService recommendationQueryService;
    private final RecommendationCommandService recommendationCommandService;
    private final UserQueryService userQueryService;

    public RecommendationController(RecommendationQueryService recommendationQueryService,
                                     RecommendationCommandService recommendationCommandService,
                                     UserQueryService userQueryService) {
        this.recommendationQueryService = recommendationQueryService;
        this.recommendationCommandService = recommendationCommandService;
        this.userQueryService = userQueryService;
    }

    /** Farmers see recommendations addressed to them; agronomists see the ones they authored. */
    @GetMapping
    public ResponseEntity<List<RecommendationResource>> getRecommendations() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        boolean isAgronomist = userQueryService.handle(new GetUserByIdQuery(userId))
                .map(u -> u.getRoles().stream().anyMatch(r -> r.getName() == Roles.ROLE_AGRONOMIST))
                .orElse(false);
        var recommendations = isAgronomist
                ? recommendationQueryService.findByAgronomistUserId(userId)
                : recommendationQueryService.findByFarmerUserId(userId);
        return ResponseEntity.ok(recommendations.stream().map(this::toResource).toList());
    }

    @PreAuthorize("hasRole('AGRONOMIST')")
    @PostMapping
    public ResponseEntity<?> createRecommendation(@RequestBody CreateRecommendationResource resource) {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        RecommendationPriority priority = resource.priority() != null
                ? RecommendationPriority.valueOf(resource.priority().toUpperCase()) : RecommendationPriority.MEDIUM;
        var command = new CreateRecommendationCommand(agronomistUserId, resource.farmerId(), resource.zoneId(),
                resource.title(), resource.description(), resource.recommendedActions(), resource.attachmentUrl(), priority);
        var result = recommendationCommandService.createRecommendation(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.CREATED);
    }

    @PatchMapping("/{recommendationId}/acknowledge")
    public ResponseEntity<?> acknowledge(@PathVariable Long recommendationId) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var result = recommendationCommandService.acknowledge(recommendationId, userId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.OK);
    }

    @PatchMapping("/{recommendationId}/dismiss")
    public ResponseEntity<?> dismiss(@PathVariable Long recommendationId) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var result = recommendationCommandService.dismiss(recommendationId, userId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.OK);
    }

    private RecommendationResource toResource(Recommendation r) {
        return new RecommendationResource(r.getId(), r.getZoneId(), r.getAgronomistUserId(),
                r.getPriority().name(), r.getStatus().name(), r.getTitle(), r.getDescription(),
                r.getRecommendedActions(), r.getGeneratedAt());
    }
}
