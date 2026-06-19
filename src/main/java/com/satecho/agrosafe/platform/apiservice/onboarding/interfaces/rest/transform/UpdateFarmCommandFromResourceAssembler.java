package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.UpdateFarmResource;

public class UpdateFarmCommandFromResourceAssembler {
    private UpdateFarmCommandFromResourceAssembler() {}
    public static UpdateFarmCommand toCommandFromResource(UpdateFarmResource resource) {
        return new UpdateFarmCommand(resource.name(), resource.location(), resource.hectares(), resource.cropType());
    }
}