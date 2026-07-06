package com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects;

/**
 * Indicates the operational state of a user's subscription.
 */
public enum SubscriptionStatus {
    /**
     * The subscription is currently active and billing successfully.
     */
    ACTIVE,

    /**
     * The subscription has been cancelled by the user.
     */
    CANCELLED,

    /**
     * The subscription period has expired.
     */
    EXPIRED
}
