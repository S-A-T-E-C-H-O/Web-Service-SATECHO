package com.satecho.agrosafe.platform.apiservice.billing.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.PlanCommandService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link PlanCommandService}.
 * Handles seeding the initial system plans on application startup.
 */
@Service
@Transactional
public class PlanCommandServiceImpl implements PlanCommandService {

    /**
     * Repository for performing database operations on {@link Plan} aggregates.
     */
    private final PlanRepository planRepository;

    /**
     * Constructs a new PlanCommandServiceImpl.
     *
     * @param planRepository the repository for managing plans
     */
    public PlanCommandServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * Idempotently ensures the default FREE/BASIC/PRO plans exist in the persistence store.
     */
    @Override
    public void seedDefaultPlans() {
        seedIfMissing(PlanTier.FREE, "Free", 0.0, 2, 1,
                List.of("Up to 2 devices", "1 farm", "Basic telemetry dashboard"));
        seedIfMissing(PlanTier.BASIC, "Basic", 19.99, 10, 3,
                List.of("Up to 10 devices", "Up to 3 farms", "Alerts and recommendations", "Email support"));
        seedIfMissing(PlanTier.PRO, "Pro", 49.99, null, null,
                List.of("Unlimited devices", "Unlimited farms", "Priority support", "Advanced analytics and reports"));
    }

    /**
     * Helper method to save a plan if a plan of that tier does not already exist.
     *
     * @param tier the {@link PlanTier} of the plan
     * @param name the descriptive name of the plan
     * @param price the monthly subscription price
     * @param maxDevices the maximum number of devices allowed under this plan, or null if unlimited
     * @param maxFarms the maximum number of farms allowed under this plan, or null if unlimited
     * @param features list of features offered in this plan tier
     */
    private void seedIfMissing(PlanTier tier, String name, Double price, Integer maxDevices, Integer maxFarms, List<String> features) {
        if (planRepository.existsByTier(tier)) return;
        planRepository.save(new Plan(tier, name, price, maxDevices, maxFarms, features));
    }
}
