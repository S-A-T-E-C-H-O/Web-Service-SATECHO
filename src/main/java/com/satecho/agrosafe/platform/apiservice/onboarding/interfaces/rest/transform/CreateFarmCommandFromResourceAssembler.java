package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.CreateFarmResource;

public class CreateFarmCommandFromResourceAssembler {
    private CreateFarmCommandFromResourceAssembler() {}
    public static CreateFarmCommand toCommandFromResource(CreateFarmResource resource, Long userId) {
        return new CreateFarmCommand(userId, resource.name(), resource.location(), resource.hectares(), resource.cropType());
    }
}