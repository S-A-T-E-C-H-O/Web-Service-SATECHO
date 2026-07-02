package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

public interface PlanLimitsService {
    /** True if the farmer's current device count is still under their plan's limit (or they have no plan → FREE default). */
    boolean canRegisterDevice(Long farmerUserId);
}
