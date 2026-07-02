package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;

public record CreateRecommendationCommand(Long agronomistUserId, Long farmerUserId, Long zoneId, String title,
                                           String description, String recommendedActions, String attachmentUrl,
                                           RecommendationPriority priority) {
}
