package com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects;

/**
 * Defines the available tiers of subscription plans in the platform.
 */
public enum PlanTier {
    /**
     * The default basic tier offered for free with restricted limits.
     */
    FREE,

    /**
     * A standard paid tier offering expanded limits and features.
     */
    BASIC,

    /**
     * The premium paid tier offering unlimited access to devices and resources.
     */
    PRO
}
