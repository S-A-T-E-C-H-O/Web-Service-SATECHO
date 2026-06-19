package com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;

import java.util.Optional;

public interface OnboardingQueryService {
    Optional<OnboardingProgress> findByUserId(Long userId);
}