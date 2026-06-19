package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

public record CompleteOnboardingCommand(Long userId) {
    public CompleteOnboardingCommand {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}