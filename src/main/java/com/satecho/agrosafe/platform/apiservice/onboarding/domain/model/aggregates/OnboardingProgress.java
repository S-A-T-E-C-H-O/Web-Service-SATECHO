package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class OnboardingProgress extends AuditableAbstractAggregateRoot<OnboardingProgress> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private boolean completed;
    @Setter private int currentStep;
    @Setter private boolean farmDataComplete;
    @Setter private boolean zoneConfigComplete;
    @Setter private boolean deviceLinkedComplete;
    @Setter private boolean thresholdsConfiguredComplete;
    @Setter private Instant completedAt;

    public OnboardingProgress() {
    }

    public OnboardingProgress(Long userId) {
        this.userId = userId;
        this.completed = false;
        this.currentStep = 1;
        this.farmDataComplete = false;
        this.zoneConfigComplete = false;
        this.deviceLinkedComplete = false;
        this.thresholdsConfiguredComplete = false;
    }

    public void advanceStep(int step, boolean farmComplete, boolean zoneComplete,
                            boolean deviceComplete, boolean thresholdsComplete) {
        this.currentStep = step;
        this.farmDataComplete = farmComplete;
        this.zoneConfigComplete = zoneComplete;
        this.deviceLinkedComplete = deviceComplete;
        this.thresholdsConfiguredComplete = thresholdsComplete;
    }

    public void markFarmDataComplete() {
        this.farmDataComplete = true;
        if (!this.zoneConfigComplete) this.currentStep = 2;
    }

    public void markZoneConfigComplete() {
        this.zoneConfigComplete = true;
        if (!this.deviceLinkedComplete) this.currentStep = 3;
    }

    public void markDeviceLinkedComplete() {
        this.deviceLinkedComplete = true;
        if (!this.thresholdsConfiguredComplete) this.currentStep = 4;
    }

    public void markThresholdsConfiguredComplete() {
        this.thresholdsConfiguredComplete = true;
        this.currentStep = 5;
    }

    public void complete() {
        this.completed = true;
        this.currentStep = 5;
        this.farmDataComplete = true;
        this.zoneConfigComplete = true;
        this.deviceLinkedComplete = true;
        this.thresholdsConfiguredComplete = true;
        this.completedAt = Instant.now();
    }
}