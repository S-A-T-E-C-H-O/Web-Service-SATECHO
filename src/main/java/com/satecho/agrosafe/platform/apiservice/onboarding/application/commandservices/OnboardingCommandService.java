package com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CompleteOnboardingCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface OnboardingCommandService {
    OnboardingProgress ensureProgress(Long userId);
    Result<OnboardingProgress, ApplicationError> completeOnboarding(CompleteOnboardingCommand command);
}