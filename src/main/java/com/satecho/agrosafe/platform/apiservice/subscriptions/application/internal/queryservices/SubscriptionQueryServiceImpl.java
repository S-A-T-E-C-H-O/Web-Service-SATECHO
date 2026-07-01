package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public Optional<Subscription> findById(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }
}
