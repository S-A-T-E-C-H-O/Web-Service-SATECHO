package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries;

public record GetRecommendationsByFarmQuery(
        Long farmId,
        String type,
        String status
) {

    public GetRecommendationsByFarmQuery {
        if (farmId == null) {
            throw new IllegalArgumentException("Farm ID is required");
        }
    }
}
