package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record AdminUserResource(
        Long id,
        String fullName,
        String email,
        String role,
        boolean verified,
        boolean enabled
) {
}
