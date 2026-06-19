package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.CreateZoneResource;

public class CreateZoneCommandFromResourceAssembler {
    private CreateZoneCommandFromResourceAssembler() {}
    public static CreateZoneCommand toCommandFromResource(CreateZoneResource resource, Long farmId) {
        return new CreateZoneCommand(farmId, resource.name(), resource.areaHectares(), resource.cropType());
    }
}