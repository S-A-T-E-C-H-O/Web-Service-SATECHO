package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import java.time.Instant;

public record SubscriptionResource(
        Long id, Long userId, String planType, String status, String billingCycle,
        Instant startDate, Instant endDate, Instant nextBillingDate, Instant trialEndDate,
        Boolean autoRenew, Instant canceledAt
) {}
