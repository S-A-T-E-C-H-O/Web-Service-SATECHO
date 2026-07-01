package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.GenerateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.GenerateRecommendationResource;

public class GenerateRecommendationCommandFromResourceAssembler {

    private GenerateRecommendationCommandFromResourceAssembler() {
    }

    public static GenerateRecommendationCommand toCommandFromResource(GenerateRecommendationResource resource, Long agronomistId) {
        return new GenerateRecommendationCommand(
                resource.farmId(),
                resource.zoneId(),
                agronomistId,
                resource.farmerId(),
                resource.type(),
                resource.priority(),
                resource.title(),
                resource.description(),
                resource.recommendedActions()
        );
    }
}
