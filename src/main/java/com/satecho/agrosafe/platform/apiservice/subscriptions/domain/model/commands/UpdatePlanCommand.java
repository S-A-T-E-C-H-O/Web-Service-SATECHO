package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.BillingCycle;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;

public record UpdatePlanCommand(
        PlanType planType,
        BillingCycle billingCycle
) {
    public UpdatePlanCommand {
        if (planType == null && billingCycle == null)
            throw new IllegalArgumentException("At least one field must be provided for update");
    }
}
