package com.satecho.agrosafe.platform.apiservice.billing.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.PlanCommandService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlanCommandServiceImpl implements PlanCommandService {

    private final PlanRepository planRepository;

    public PlanCommandServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public void seedDefaultPlans() {
        seedIfMissing(PlanTier.FREE, "Free", 0.0, 2, 1,
                List.of("Up to 2 devices", "1 farm", "Basic telemetry dashboard"));
        seedIfMissing(PlanTier.BASIC, "Basic", 19.99, 10, 3,
                List.of("Up to 10 devices", "Up to 3 farms", "Alerts and recommendations", "Email support"));
        seedIfMissing(PlanTier.PRO, "Pro", 49.99, null, null,
                List.of("Unlimited devices", "Unlimited farms", "Priority support", "Advanced analytics and reports"));
    }

    private void seedIfMissing(PlanTier tier, String name, Double price, Integer maxDevices, Integer maxFarms, List<String> features) {
        if (planRepository.existsByTier(tier)) return;
        planRepository.save(new Plan(tier, name, price, maxDevices, maxFarms, features));
    }
}
