package com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> findByUserId(Long userId);
    Optional<Subscription> findById(Long subscriptionId);
}
