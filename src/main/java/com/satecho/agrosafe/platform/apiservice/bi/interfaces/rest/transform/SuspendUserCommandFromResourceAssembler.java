package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.SuspendUserCommand;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.SuspendUserResource;

public class SuspendUserCommandFromResourceAssembler {

    private SuspendUserCommandFromResourceAssembler() {
    }

    public static SuspendUserCommand toCommandFromResource(SuspendUserResource resource, Long userId, Long suspendedBy) {
        return new SuspendUserCommand(userId, resource.reason(), suspendedBy);
    }
}
