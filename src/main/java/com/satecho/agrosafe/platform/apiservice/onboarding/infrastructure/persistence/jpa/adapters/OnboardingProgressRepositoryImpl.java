package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.OnboardingProgressRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers.OnboardingProgressPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories.OnboardingProgressPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OnboardingProgressRepositoryImpl implements OnboardingProgressRepository {

    private final OnboardingProgressPersistenceRepository persistenceRepository;

    public OnboardingProgressRepositoryImpl(OnboardingProgressPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<OnboardingProgress> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId)
                .map(OnboardingProgressPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public OnboardingProgress save(OnboardingProgress progress) {
        var saved = persistenceRepository.save(OnboardingProgressPersistenceAssembler.toPersistenceFromDomain(progress));
        return OnboardingProgressPersistenceAssembler.toDomainFromPersistence(saved);
    }
}