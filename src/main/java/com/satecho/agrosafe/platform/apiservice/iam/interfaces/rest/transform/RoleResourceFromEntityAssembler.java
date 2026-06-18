package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.RoleResource;

/**
 * Assembles a {@link RoleResource} from a {@link Role} entity.
 */
public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role role) {
        return new RoleResource(role.getId(), role.getStringName());
    }
}