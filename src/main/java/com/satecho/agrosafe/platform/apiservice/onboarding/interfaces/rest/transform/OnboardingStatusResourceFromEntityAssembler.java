package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.OnboardingStatusResource;

public class OnboardingStatusResourceFromEntityAssembler {
    private OnboardingStatusResourceFromEntityAssembler() {}
    public static OnboardingStatusResource toResourceFromEntity(OnboardingProgress entity) {
        return new OnboardingStatusResource(entity.getId(), entity.getUserId(), entity.isCompleted(),
                entity.getCurrentStep(), entity.isFarmDataComplete(), entity.isZoneConfigComplete(),
                entity.isDeviceLinkedComplete(), entity.isThresholdsConfiguredComplete(),
                entity.getCompletedAt());
    }
}