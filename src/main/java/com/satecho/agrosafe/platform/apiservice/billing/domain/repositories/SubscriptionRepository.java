package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;

import java.util.Optional;

/**
 * Repository interface for managing Subscription aggregates.
 */
public interface SubscriptionRepository {
    /**
     * Persists or updates a Subscription.
     *
     * @param subscription the subscription to save
     * @return the saved {@link Subscription} aggregate
     */
    Subscription save(Subscription subscription);

    /**
     * Finds the subscription record for a specific user ID.
     *
     * @param userId the user ID of the subscriber
     * @return an {@link Optional} containing the subscription if found, or empty
     */
    Optional<Subscription> findByUserId(Long userId);
}
