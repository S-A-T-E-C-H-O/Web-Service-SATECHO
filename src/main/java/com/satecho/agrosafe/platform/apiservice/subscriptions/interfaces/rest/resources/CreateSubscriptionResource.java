package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.BillingCycle;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;

public record CreateSubscriptionResource(PlanType planType, BillingCycle billingCycle) {}
