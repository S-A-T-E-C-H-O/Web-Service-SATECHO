package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.queries;

public record GetAllZonesByFarmIdQuery(Long farmId) {
    public GetAllZonesByFarmIdQuery {
        if (farmId == null) throw new IllegalArgumentException("Farm ID is required");
    }
}