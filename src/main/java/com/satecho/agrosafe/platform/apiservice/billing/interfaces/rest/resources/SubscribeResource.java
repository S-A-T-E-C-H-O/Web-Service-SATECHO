package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

/**
 * REST request resource representation for a subscription action.
 *
 * @param planTier the tier name the user wishes to subscribe to (e.g. BASIC, PRO)
 */
public record SubscribeResource(String planTier) {
}
