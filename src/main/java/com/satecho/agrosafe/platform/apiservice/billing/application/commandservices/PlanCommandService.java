package com.satecho.agrosafe.platform.apiservice.billing.application.commandservices;

/**
 * Service interface for handling plan seeding commands.
 */
public interface PlanCommandService {
    /**
     * Idempotently ensures the default FREE, BASIC, and PRO plans exist in the system.
     */
    void seedDefaultPlans();
}
