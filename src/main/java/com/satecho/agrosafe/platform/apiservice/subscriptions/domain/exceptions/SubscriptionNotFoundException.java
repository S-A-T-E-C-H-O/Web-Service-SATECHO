package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class SubscriptionNotFoundException extends AgroSafeException {

    public SubscriptionNotFoundException(Long subscriptionId) {
        super("Subscription not found with ID: " + subscriptionId);
    }

    public SubscriptionNotFoundException(String message) {
        super(message);
    }
}
