package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.PlanResource;

public class PlanResourceFromPlanTypeAssembler {
    private PlanResourceFromPlanTypeAssembler() {}

    public static PlanResource toResourceFromPlanType(PlanType planType) {
        return new PlanResource(planType.name(), planType.getPricePerHaPerMonth(),
                planType.getMaxFarms(), planType.getMaxDevices(), planType.getFeatures());
    }
}
