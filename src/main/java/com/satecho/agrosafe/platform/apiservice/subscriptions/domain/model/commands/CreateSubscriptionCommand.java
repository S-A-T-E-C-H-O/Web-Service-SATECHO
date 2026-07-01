package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.BillingCycle;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;

public record CreateSubscriptionCommand(
        Long userId,
        PlanType planType,
        BillingCycle billingCycle
) {
    public CreateSubscriptionCommand {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (planType == null) throw new IllegalArgumentException("Plan type is required");
        if (billingCycle == null) throw new IllegalArgumentException("Billing cycle is required");
    }
}
