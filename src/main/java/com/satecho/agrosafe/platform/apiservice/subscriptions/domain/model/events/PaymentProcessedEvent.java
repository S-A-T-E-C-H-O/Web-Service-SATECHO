package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events;

import java.time.Instant;

public record PaymentProcessedEvent(
        Long paymentId,
        Long userId,
        Long subscriptionId,
        Long invoiceId,
        Double amount,
        String currency,
        String paymentMethod,
        String transactionId,
        Instant processedAt,
        Instant occurredAt
) {
    public PaymentProcessedEvent(Long paymentId, Long userId, Long subscriptionId, Long invoiceId,
                                  Double amount, String currency, String paymentMethod,
                                  String transactionId, Instant processedAt) {
        this(paymentId, userId, subscriptionId, invoiceId, amount, currency,
                paymentMethod, transactionId, processedAt, Instant.now());
    }
}
