package com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands;

public record VerifyAccountCommand(String token) {
    public VerifyAccountCommand {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token is required");
    }
}