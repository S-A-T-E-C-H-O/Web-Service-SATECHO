package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.PaymentResource;

public class PaymentResourceFromEntityAssembler {
    private PaymentResourceFromEntityAssembler() {}

    public static PaymentResource toResourceFromEntity(Payment entity) {
        return new PaymentResource(
                entity.getId(), entity.getUserId(), entity.getSubscriptionId(),
                entity.getInvoiceId(), entity.getAmount(), entity.getCurrency(),
                entity.getPaymentMethod().name(), entity.getStatus(),
                entity.getTransactionId(), entity.getProcessedAt());
    }
}
