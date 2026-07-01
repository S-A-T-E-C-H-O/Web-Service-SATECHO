package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.CreateSubscriptionResource;

public class CreateSubscriptionCommandFromResourceAssembler {
    private CreateSubscriptionCommandFromResourceAssembler() {}

    public static CreateSubscriptionCommand toCommandFromResource(CreateSubscriptionResource resource, Long userId) {
        return new CreateSubscriptionCommand(userId, resource.planType(), resource.billingCycle());
    }
}
