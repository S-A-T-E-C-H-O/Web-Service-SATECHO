package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanLimitsService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class PlanLimitsServiceImpl implements PlanLimitsService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final DeviceRepository deviceRepository;

    public PlanLimitsServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository,
                                  DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public boolean canRegisterDevice(Long farmerUserId) {
        var plan = subscriptionRepository.findByUserId(farmerUserId)
                .flatMap(sub -> planRepository.findById(sub.getPlanId()))
                .or(() -> planRepository.findByTier(PlanTier.BASIC));
        if (plan.isEmpty()) return true; // no plans seeded yet — fail open rather than block onboarding
        int currentDeviceCount = deviceRepository.findByUserId(farmerUserId).size();
        return plan.get().allowsMoreDevices(currentDeviceCount);
    }
}
