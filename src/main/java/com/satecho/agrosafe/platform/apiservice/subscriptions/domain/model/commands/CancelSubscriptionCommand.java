package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands;

public record CancelSubscriptionCommand(
        Long subscriptionId,
        Long userId
) {
    public CancelSubscriptionCommand {
        if (subscriptionId == null) throw new IllegalArgumentException("Subscription ID is required");
        if (userId == null) throw new IllegalArgumentException("User ID is required");
    }
}
