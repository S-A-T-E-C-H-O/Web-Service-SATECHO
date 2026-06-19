package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

import java.time.Instant;

public record OnboardingStatusResource(Long id, Long userId, boolean completed, int currentStep,
                                       boolean farmDataComplete, boolean zoneConfigComplete,
                                       boolean deviceLinkedComplete, boolean thresholdsConfiguredComplete,
                                       Instant completedAt) {}