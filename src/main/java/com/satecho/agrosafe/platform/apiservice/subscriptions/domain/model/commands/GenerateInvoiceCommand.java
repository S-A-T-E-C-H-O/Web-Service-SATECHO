package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands;

import java.time.Instant;

public record GenerateInvoiceCommand(
        Long userId,
        Long subscriptionId,
        Double amount,
        String description,
        Instant billingPeriodStart,
        Instant billingPeriodEnd,
        Instant dueDate
) {
    public GenerateInvoiceCommand {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (subscriptionId == null) throw new IllegalArgumentException("Subscription ID is required");
        if (amount == null || amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        if (billingPeriodStart == null) throw new IllegalArgumentException("Billing period start is required");
        if (billingPeriodEnd == null) throw new IllegalArgumentException("Billing period end is required");
        if (dueDate == null) throw new IllegalArgumentException("Due date is required");
    }
}
