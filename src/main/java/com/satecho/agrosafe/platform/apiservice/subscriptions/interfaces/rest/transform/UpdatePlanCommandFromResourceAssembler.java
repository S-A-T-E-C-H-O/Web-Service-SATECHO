package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.UpdatePlanCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.UpdatePlanResource;

public class UpdatePlanCommandFromResourceAssembler {
    private UpdatePlanCommandFromResourceAssembler() {}

    public static UpdatePlanCommand toCommandFromResource(UpdatePlanResource resource) {
        return new UpdatePlanCommand(resource.planType(), resource.billingCycle());
    }
}
