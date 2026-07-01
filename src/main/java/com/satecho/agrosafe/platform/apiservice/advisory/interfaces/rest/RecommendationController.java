package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.RecommendationCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.RecommendationQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions.RecommendationNotFoundException;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationType;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.GenerateRecommendationResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.RecommendationResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.GenerateRecommendationCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.RecommendationResourceFromEntityAssembler;
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
@Tag(name = "Recommendations", description = "Recommendation management endpoints")
@PreAuthorize("isAuthenticated()")
public class RecommendationController {

    private final RecommendationCommandService recommendationCommandService;
    private final RecommendationQueryService recommendationQueryService;

    public RecommendationController(RecommendationCommandService recommendationCommandService,
                                    RecommendationQueryService recommendationQueryService) {
        this.recommendationCommandService = recommendationCommandService;
        this.recommendationQueryService = recommendationQueryService;
    }

    @PostMapping
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<?> generateRecommendation(@RequestBody GenerateRecommendationResource resource) {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var command = GenerateRecommendationCommandFromResourceAssembler.toCommandFromResource(resource, agronomistId);
        var result = recommendationCommandService.generateRecommendation(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                RecommendationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RecommendationResource>> getRecommendations(
            @RequestParam(required = false) Long farmId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {

        List<Recommendation> recommendations;

        if (farmId != null) {
            recommendations = recommendationQueryService.findByFarmId(farmId);
        } else {
            Long userId = SecurityContextUtil.getCurrentUserId();
            recommendations = recommendationQueryService.findByFarmerId(userId);
        }

        if (type != null && !type.isBlank()) {
            try {
                RecommendationType recommendationType = RecommendationType.valueOf(type.toUpperCase());
                recommendations = recommendations.stream().filter(r -> r.getType() == recommendationType).toList();
            } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }

        if (status != null && !status.isBlank()) {
            try {
                RecommendationStatus recommendationStatus = RecommendationStatus.valueOf(status.toUpperCase());
                recommendations = recommendations.stream().filter(r -> r.getStatus() == recommendationStatus).toList();
            } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }

        return ResponseEntity.ok(recommendations.stream()
                .map(RecommendationResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @GetMapping("/{recommendationId}")
    public ResponseEntity<RecommendationResource> getRecommendationById(@PathVariable Long recommendationId) {
        return recommendationQueryService.findById(recommendationId)
                .map(r -> ResponseEntity.ok(RecommendationResourceFromEntityAssembler.toResourceFromEntity(r)))
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
    }

    @PatchMapping("/{recommendationId}/acknowledge")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<?> acknowledgeRecommendation(@PathVariable Long recommendationId) {
        Long farmerId = SecurityContextUtil.getCurrentUserId();
        var result = recommendationCommandService.acknowledgeRecommendation(recommendationId, farmerId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                RecommendationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PatchMapping("/{recommendationId}/dismiss")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<?> dismissRecommendation(@PathVariable Long recommendationId) {
        Long farmerId = SecurityContextUtil.getCurrentUserId();
        var result = recommendationCommandService.dismissRecommendation(recommendationId, farmerId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                RecommendationResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
