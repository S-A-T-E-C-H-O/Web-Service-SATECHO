package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
    Optional<Subscription> findByUserId(Long userId);
}
