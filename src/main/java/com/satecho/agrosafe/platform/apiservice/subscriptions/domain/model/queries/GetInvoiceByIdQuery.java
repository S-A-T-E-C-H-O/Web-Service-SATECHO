package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.queries;

public record GetInvoiceByIdQuery(Long invoiceId) {
    public GetInvoiceByIdQuery {
        if (invoiceId == null) throw new IllegalArgumentException("Invoice ID is required");
    }
}
