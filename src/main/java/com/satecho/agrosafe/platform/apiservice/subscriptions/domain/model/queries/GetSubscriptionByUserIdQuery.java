package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.queries;

public record GetSubscriptionByUserIdQuery(Long userId) {
    public GetSubscriptionByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}
