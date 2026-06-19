package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.OnboardingQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.OnboardingProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class OnboardingQueryServiceImpl implements OnboardingQueryService {

    private final OnboardingProgressRepository onboardingProgressRepository;

    public OnboardingQueryServiceImpl(OnboardingProgressRepository onboardingProgressRepository) {
        this.onboardingProgressRepository = onboardingProgressRepository;
    }

    @Override
    public Optional<OnboardingProgress> findByUserId(Long userId) {
        return onboardingProgressRepository.findByUserId(userId);
    }
}