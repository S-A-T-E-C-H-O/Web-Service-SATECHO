package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link SubscriptionQueryService}.
 * Handles querying user subscription states.
 */
@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    /**
     * Repository for executing queries on subscriptions.
     */
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Constructs a new SubscriptionQueryServiceImpl.
     *
     * @param subscriptionRepository the subscription repository
     */
    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Finds the active subscription for a specific user.
     *
     * @param userId the ID of the user
     * @return an {@link Optional} containing the {@link Subscription} if found, or empty
     */
    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
}
