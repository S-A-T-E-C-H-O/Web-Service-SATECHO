package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;

import java.util.Optional;

/**
 * Query service interface for retrieving subscription status.
 */
public interface SubscriptionQueryService {
    /**
     * Finds the subscription profile of a specific user.
     *
     * @param userId the ID of the user
     * @return an {@link Optional} containing the {@link Subscription} if found, or empty
     */
    Optional<Subscription> findByUserId(Long userId);
}
