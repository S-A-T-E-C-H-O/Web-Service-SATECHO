package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries;

public record GetRecommendationsByUserIdQuery(
        Long userId
) {

    public GetRecommendationsByUserIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}
