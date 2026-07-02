package com.satecho.agrosafe.platform.apiservice.billing.application.commandservices;

public interface PlanCommandService {
    /** Idempotently ensures the default FREE/BASIC/PRO plans exist. */
    void seedDefaultPlans();
}
