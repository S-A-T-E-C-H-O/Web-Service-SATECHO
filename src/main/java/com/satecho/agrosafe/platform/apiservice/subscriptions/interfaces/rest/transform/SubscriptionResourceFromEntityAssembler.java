package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.SubscriptionResource;

public class SubscriptionResourceFromEntityAssembler {
    private SubscriptionResourceFromEntityAssembler() {}

    public static SubscriptionResource toResourceFromEntity(Subscription entity) {
        return new SubscriptionResource(
                entity.getId(), entity.getUserId(), entity.getPlanType().name(),
                entity.getStatus().name(), entity.getBillingCycle().name(),
                entity.getStartDate(), entity.getEndDate(), entity.getNextBillingDate(),
                entity.getTrialEndDate(), entity.getAutoRenew(), entity.getCanceledAt());
    }
}
