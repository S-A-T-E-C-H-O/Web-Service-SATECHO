package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationType;

public record GenerateRecommendationResource(
        Long farmId,
        Long zoneId,
        Long farmerId,
        RecommendationType type,
        RecommendationPriority priority,
        String title,
        String description,
        String recommendedActions
) {
}
