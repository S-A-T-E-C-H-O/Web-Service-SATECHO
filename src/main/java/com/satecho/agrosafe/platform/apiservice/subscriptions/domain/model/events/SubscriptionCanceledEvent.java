package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events;

import java.time.Instant;

public record SubscriptionCanceledEvent(
        Long subscriptionId,
        Long userId,
        Instant canceledAt,
        Instant occurredAt
) {
    public SubscriptionCanceledEvent(Long subscriptionId, Long userId, Instant canceledAt) {
        this(subscriptionId, userId, canceledAt, Instant.now());
    }
}
