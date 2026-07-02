package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.SignUpResource;

import java.util.ArrayList;

/**
 * Assembles a {@link SignUpCommand} from a {@link SignUpResource}.
 */
public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        var roles = resource.roles() != null
                ? resource.roles().stream().map(Role::toRoleFromName).toList()
                : new ArrayList<Role>();
        return new SignUpCommand(resource.fullName(), resource.email(), resource.password(), roles,
                resource.registrationNumber(), resource.specialty(), resource.yearsOfExperience());
    }
}