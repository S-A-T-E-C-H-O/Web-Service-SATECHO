package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanLimitsService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link PlanLimitsService}.
 * Handles checking subscription-based limits like maximum allowed IoT devices.
 */
@Service
public class PlanLimitsServiceImpl implements PlanLimitsService {

    /**
     * Repository for checking a user's subscription.
     */
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Repository for retrieving plan limits configuration.
     */
    private final PlanRepository planRepository;

    /**
     * Repository for counting a user's registered IoT devices.
     */
    private final DeviceRepository deviceRepository;

    /**
     * Constructs a new PlanLimitsServiceImpl.
     *
     * @param subscriptionRepository the subscription repository
     * @param planRepository the plan repository
     * @param deviceRepository the device repository
     */
    public PlanLimitsServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository,
                                  DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Checks if a user is allowed to register an additional IoT device under their active plan limits.
     *
     * @param farmerUserId the user ID of the farmer
     * @return true if device registration is permitted; false otherwise
     */
    @Override
    public boolean canRegisterDevice(Long farmerUserId) {
        var plan = subscriptionRepository.findByUserId(farmerUserId)
                .flatMap(sub -> planRepository.findById(sub.getPlanId()))
                .or(() -> planRepository.findByTier(PlanTier.FREE));
        if (plan.isEmpty()) return true; // no plans seeded yet — fail open rather than block onboarding
        int currentDeviceCount = deviceRepository.findByUserId(farmerUserId).size();
        return plan.get().allowsMoreDevices(currentDeviceCount);
    }
}
