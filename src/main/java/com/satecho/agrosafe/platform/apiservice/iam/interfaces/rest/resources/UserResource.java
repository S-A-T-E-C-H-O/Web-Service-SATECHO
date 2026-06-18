package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import java.util.Date;
import java.util.List;

public record UserResource(
        Long id,
        String fullName,
        String email,
        List<String> roles,
        Boolean verified,
        Date createdAt
) {
}