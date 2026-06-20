package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.AcknowledgeSecurityEventCommand;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.AcknowledgeEventResource;

public class AcknowledgeSecurityEventCommandFromResourceAssembler {
    private AcknowledgeSecurityEventCommandFromResourceAssembler() {}
    public static AcknowledgeSecurityEventCommand toCommand(Long eventId, AcknowledgeEventResource resource) {
        return new AcknowledgeSecurityEventCommand(eventId, resource.acknowledgedBy());
    }
}
