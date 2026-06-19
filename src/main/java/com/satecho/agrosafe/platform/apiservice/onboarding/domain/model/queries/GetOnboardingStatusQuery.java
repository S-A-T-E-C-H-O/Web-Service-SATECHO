package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.queries;

public record GetOnboardingStatusQuery(Long userId) {
    public GetOnboardingStatusQuery {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}