package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.FarmResource;

public class FarmResourceFromEntityAssembler {
    private FarmResourceFromEntityAssembler() {}
    public static FarmResource toResourceFromEntity(Farm entity) {
        return new FarmResource(entity.getId(), entity.getUserId(), entity.getName(),
                entity.getLocation(), entity.getHectares(), entity.getCropType().name());
    }
}