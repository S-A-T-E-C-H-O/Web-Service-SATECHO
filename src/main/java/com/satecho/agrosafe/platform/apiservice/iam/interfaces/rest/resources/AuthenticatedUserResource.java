package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import java.util.List;

/**
 * Resource returned after successful authentication.
 */
public record AuthenticatedUserResource(
        Long id,
        String fullName,
        String email,
        String token,
        List<String> roles
) {
}