package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.UpdateSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.UpdateSubscriptionResource;

public class UpdateSubscriptionCommandFromResourceAssembler {
    private UpdateSubscriptionCommandFromResourceAssembler() {}

    public static UpdateSubscriptionCommand toCommandFromResource(UpdateSubscriptionResource resource) {
        return new UpdateSubscriptionCommand(resource.planType(), resource.billingCycle());
    }
}
