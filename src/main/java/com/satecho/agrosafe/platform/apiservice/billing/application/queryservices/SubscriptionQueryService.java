package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;

import java.util.Optional;

public interface SubscriptionQueryService {
    Optional<Subscription> findByUserId(Long userId);
}
