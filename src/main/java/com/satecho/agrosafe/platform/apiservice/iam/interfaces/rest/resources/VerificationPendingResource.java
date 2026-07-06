package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import java.time.Instant;

/** Safe response returned while an account is waiting for email ownership verification. */
public record VerificationPendingResource(
        String code,
        String email,
        Instant expiresAt,
        boolean canResend,
        String message
) { }
