package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.RecommendationResource;

public class RecommendationResourceFromEntityAssembler {

    private RecommendationResourceFromEntityAssembler() {
    }

    public static RecommendationResource toResourceFromEntity(Recommendation entity) {
        return new RecommendationResource(
                entity.getId(),
                entity.getFarmId(),
                entity.getZoneId(),
                entity.getAgronomistId(),
                entity.getFarmerId(),
                entity.getType().name(),
                entity.getPriority().name(),
                entity.getStatus().name(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getRecommendedActions(),
                entity.getGeneratedAt(),
                entity.getSentAt(),
                entity.getAcknowledgedAt(),
                entity.getAcknowledgedBy(),
                null
        );
    }
}
