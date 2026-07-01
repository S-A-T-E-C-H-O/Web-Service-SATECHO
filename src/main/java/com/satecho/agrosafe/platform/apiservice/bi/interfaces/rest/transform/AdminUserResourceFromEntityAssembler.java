package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.AdminUserResource;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;

public class AdminUserResourceFromEntityAssembler {

    private AdminUserResourceFromEntityAssembler() {
    }

    public static AdminUserResource toResourceFromEntity(User entity) {
        return new AdminUserResource(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getRoles().stream().findFirst()
                        .map(r -> r.getName().name())
                        .orElse("UNKNOWN"),
                true,
                true
        );
    }
}
