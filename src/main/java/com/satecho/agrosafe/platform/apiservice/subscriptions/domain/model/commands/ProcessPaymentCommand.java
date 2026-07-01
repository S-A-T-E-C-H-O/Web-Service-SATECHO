package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PaymentMethod;

public record ProcessPaymentCommand(
        Long userId,
        Long subscriptionId,
        Long invoiceId,
        Double amount,
        String currency,
        PaymentMethod paymentMethod,
        String externalTransactionId
) {
    public ProcessPaymentCommand {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (subscriptionId == null) throw new IllegalArgumentException("Subscription ID is required");
        if (amount == null || amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency is required");
        if (paymentMethod == null) throw new IllegalArgumentException("Payment method is required");
        if (externalTransactionId == null || externalTransactionId.isBlank())
            throw new IllegalArgumentException("External transaction ID is required");
    }
}
