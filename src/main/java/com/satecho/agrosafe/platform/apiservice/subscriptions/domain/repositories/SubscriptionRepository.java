package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionRepository {
    Optional<Subscription> findById(Long id);
    Optional<Subscription> findByUserId(Long userId);
    Subscription save(Subscription subscription);
    boolean existsById(Long id);
}
