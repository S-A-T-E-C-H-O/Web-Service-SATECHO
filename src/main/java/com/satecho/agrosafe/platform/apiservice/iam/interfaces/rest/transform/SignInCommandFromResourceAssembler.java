package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.SignInResource;

/**
 * Assembles a {@link SignInCommand} from a {@link SignInResource}.
 */
public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }
}