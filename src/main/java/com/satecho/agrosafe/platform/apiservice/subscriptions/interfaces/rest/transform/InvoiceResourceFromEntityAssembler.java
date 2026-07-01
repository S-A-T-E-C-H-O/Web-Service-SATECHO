package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.InvoiceResource;

public class InvoiceResourceFromEntityAssembler {
    private InvoiceResourceFromEntityAssembler() {}

    public static InvoiceResource toResourceFromEntity(Invoice entity) {
        return new InvoiceResource(
                entity.getId(), entity.getUserId(), entity.getSubscriptionId(),
                entity.getAmount(), entity.getCurrency(), entity.getDescription(),
                entity.getStatus().name(), entity.getBillingPeriodStart(),
                entity.getBillingPeriodEnd(), entity.getDueDate(), entity.getPaidAt(),
                entity.getPaymentMethod(), entity.getTransactionId());
    }
}
