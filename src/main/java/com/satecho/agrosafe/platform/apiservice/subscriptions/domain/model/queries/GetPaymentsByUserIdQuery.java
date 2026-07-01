package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.queries;

public record GetPaymentsByUserIdQuery(Long userId) {
    public GetPaymentsByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}
