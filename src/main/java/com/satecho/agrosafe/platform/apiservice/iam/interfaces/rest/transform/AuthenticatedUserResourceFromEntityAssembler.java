package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.AuthenticatedUserResource;

/**
 * Assembles an {@link AuthenticatedUserResource} from a {@link User} entity and token.
 */
public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        var roles = user.getRoles().stream()
                .map(role -> role.getStringName())
                .toList();
        return new AuthenticatedUserResource(user.getId(), user.getFullName(), user.getEmail(), token, roles);
    }
}