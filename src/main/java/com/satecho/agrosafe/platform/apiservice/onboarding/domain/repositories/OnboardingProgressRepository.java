package com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;

import java.util.Optional;

public interface OnboardingProgressRepository {
    Optional<OnboardingProgress> findByUserId(Long userId);
    OnboardingProgress save(OnboardingProgress progress);
}