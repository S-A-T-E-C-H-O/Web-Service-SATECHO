package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import java.util.List;

/** Result of a successful email verification. */
public record VerificationResultResource(
        Long id,
        String fullName,
        String email,
        List<String> roles,
        String token
) { }
