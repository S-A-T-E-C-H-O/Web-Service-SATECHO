package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.UserResource;

/**
 * Assembles a {@link UserResource} from a {@link User} entity.
 */
public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        var roles = user.getRoles().stream()
                .map(role -> role.getStringName())
                .toList();
        return new UserResource(user.getId(), user.getFullName(), user.getEmail(), roles,
                user.getVerified() != null ? user.getVerified() : true,
                user.getBlocked() != null ? user.getBlocked() : false, user.getCreatedAt());
    }
}