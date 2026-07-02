package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionQueryServiceImpl implements SubscriptionQueryService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionQueryServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }
}
