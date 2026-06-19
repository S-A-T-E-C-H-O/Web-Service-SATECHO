package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.queries;

public record GetAllFarmsByUserIdQuery(Long userId) {
    public GetAllFarmsByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}