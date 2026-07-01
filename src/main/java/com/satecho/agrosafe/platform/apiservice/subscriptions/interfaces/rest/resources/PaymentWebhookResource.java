package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PaymentMethod;

public record PaymentWebhookResource(
        Long userId, Long subscriptionId, Long invoiceId, Double amount,
        String currency, PaymentMethod paymentMethod, String externalTransactionId, String signature
) {}
