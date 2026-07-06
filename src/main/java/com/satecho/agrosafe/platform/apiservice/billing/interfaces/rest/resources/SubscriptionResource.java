package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

/**
 * REST response resource representation of a Subscription.
 * Field names map exactly to the Mobile App models.
 *
 * @param id the subscription identifier
 * @param planType the subscribed plan tier key name
 * @param status the active status of the subscription
 * @param billingCycle the billing frequency (e.g. MONTHLY)
 */
public record SubscriptionResource(Long id, String planType, String status, String billingCycle) {
}
