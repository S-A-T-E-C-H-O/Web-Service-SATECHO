package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

/** Field names match Mobile App's SubscriptionModel.fromJson exactly. */
public record SubscriptionResource(Long id, String planType, String status, String billingCycle) {
}
