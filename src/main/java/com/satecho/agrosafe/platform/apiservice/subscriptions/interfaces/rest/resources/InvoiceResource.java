package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import java.time.Instant;

public record InvoiceResource(
        Long id, Long userId, Long subscriptionId, Double amount, String currency,
        String description, String status, Instant billingPeriodStart, Instant billingPeriodEnd,
        Instant dueDate, Instant paidAt, String paymentMethod, String transactionId
) {}
