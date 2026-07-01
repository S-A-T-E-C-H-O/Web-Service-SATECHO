package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationType;

public record GenerateRecommendationCommand(
        Long farmId,
        Long zoneId,
        Long agronomistId,
        Long farmerId,
        RecommendationType type,
        RecommendationPriority priority,
        String title,
        String description,
        String recommendedActions
) {

    public GenerateRecommendationCommand {
        if (farmId == null) {
            throw new IllegalArgumentException("Farm ID is required");
        }
        if (agronomistId == null) {
            throw new IllegalArgumentException("Agronomist ID is required");
        }
        if (farmerId == null) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Recommendation type is required");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Recommendation priority is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }
        if (recommendedActions == null || recommendedActions.isBlank()) {
            throw new IllegalArgumentException("Recommended actions are required");
        }
    }
}
