package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events;

import java.time.Instant;

public record SubscriptionCreatedEvent(
        Long subscriptionId,
        Long userId,
        String planType,
        String billingCycle,
        Instant occurredAt
) {
    public SubscriptionCreatedEvent(Long subscriptionId, Long userId, String planType, String billingCycle) {
        this(subscriptionId, userId, planType, billingCycle, Instant.now());
    }
}
