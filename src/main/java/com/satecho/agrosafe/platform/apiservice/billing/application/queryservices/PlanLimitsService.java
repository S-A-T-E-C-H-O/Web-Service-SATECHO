package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

/**
 * Query service interface for evaluating user plan limits.
 */
public interface PlanLimitsService {
    /**
     * Checks if the farmer's current device count is still under their plan's limit
     * (or they have no plan which resolves to the FREE tier default limit).
     *
     * @param farmerUserId the user ID of the farmer
     * @return true if the farmer can register another device, false otherwise
     */
    boolean canRegisterDevice(Long farmerUserId);
}
