package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.queries;

public record GetFarmByIdQuery(Long farmId) {
    public GetFarmByIdQuery {
        if (farmId == null) throw new IllegalArgumentException("Farm ID is required");
    }
}