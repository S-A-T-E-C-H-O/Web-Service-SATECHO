package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * Resource for sign-up requests.
 */
public record SignUpResource(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 50) String email,
        @NotBlank @Size(min = 8, max = 120) String password,
        List<String> roles,
        // Required only when roles includes ROLE_AGRONOMIST (EP-001-US007).
        String registrationNumber,
        String specialty,
        Integer yearsOfExperience
) {
}