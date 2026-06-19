package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.OnboardingProgressPersistenceEntity;

public final class OnboardingProgressPersistenceAssembler {

    private OnboardingProgressPersistenceAssembler() {
    }

    public static OnboardingProgress toDomainFromPersistence(OnboardingProgressPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new OnboardingProgress();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setCompleted(entity.isCompleted());
        domain.setCurrentStep(entity.getCurrentStep());
        domain.setFarmDataComplete(entity.isFarmDataComplete());
        domain.setZoneConfigComplete(entity.isZoneConfigComplete());
        domain.setDeviceLinkedComplete(entity.isDeviceLinkedComplete());
        domain.setThresholdsConfiguredComplete(entity.isThresholdsConfiguredComplete());
        domain.setCompletedAt(entity.getCompletedAt());
        return domain;
    }

    public static OnboardingProgressPersistenceEntity toPersistenceFromDomain(OnboardingProgress progress) {
        if (progress == null) return null;
        var entity = new OnboardingProgressPersistenceEntity();
        if (progress.getId() != null) {
            entity.setId(progress.getId());
        }
        entity.setUserId(progress.getUserId());
        entity.setCompleted(progress.isCompleted());
        entity.setCurrentStep(progress.getCurrentStep());
        entity.setFarmDataComplete(progress.isFarmDataComplete());
        entity.setZoneConfigComplete(progress.isZoneConfigComplete());
        entity.setDeviceLinkedComplete(progress.isDeviceLinkedComplete());
        entity.setThresholdsConfiguredComplete(progress.isThresholdsConfiguredComplete());
        entity.setCompletedAt(progress.getCompletedAt());
        return entity;
    }
}