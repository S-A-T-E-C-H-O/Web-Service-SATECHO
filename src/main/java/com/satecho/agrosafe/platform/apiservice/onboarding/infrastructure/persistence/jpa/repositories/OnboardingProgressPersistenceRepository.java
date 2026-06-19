package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.OnboardingProgressPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnboardingProgressPersistenceRepository extends JpaRepository<OnboardingProgressPersistenceEntity, Long> {
    Optional<OnboardingProgressPersistenceEntity> findByUserId(Long userId);
}