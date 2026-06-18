package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Resource for sign-in requests.
 */
public record SignInResource(
        @NotBlank String email,
        @NotBlank String password
) {
}