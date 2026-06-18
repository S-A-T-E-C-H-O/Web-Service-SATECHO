package com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands;

public record ResendVerificationCommand(String email) {
    public ResendVerificationCommand {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email is required");
    }
}