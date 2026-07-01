package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import java.time.Instant;

public record PaymentResource(
        Long id, Long userId, Long subscriptionId, Long invoiceId, Double amount,
        String currency, String paymentMethod, String status, String transactionId,
        Instant processedAt
) {}
