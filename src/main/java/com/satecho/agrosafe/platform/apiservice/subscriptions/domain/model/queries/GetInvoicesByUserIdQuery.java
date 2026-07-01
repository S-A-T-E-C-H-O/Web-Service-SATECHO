package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.queries;

public record GetInvoicesByUserIdQuery(Long userId) {
    public GetInvoicesByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}
