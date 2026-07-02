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
        seedIfMissing(PlanTier.BASIC, "Basic", 39.90, 2, 1, 2,
                List.of("Hasta 2 hectáreas", "Sensores estándar (2 nodos)", "Reportes semanales PDF"));
        seedIfMissing(PlanTier.PRO, "Pro", 79.99, 10, 3, 20,
                List.of("Hasta 20 hectáreas", "Automatización completa", "Alertas SMS y Push en tiempo real", "App Premium Multi-dispositivo"));
        seedIfMissing(PlanTier.PREMIUM, "Enterprise", 199.99, null, null, null,
                List.of("Hectáreas ilimitadas", "API de integración personalizada", "Soporte 24/7 con técnico asignado", "Capacitación in-situ para equipos"));
    }

    private void seedIfMissing(PlanTier tier, String name, Double price, Integer maxDevices, Integer maxFarms, Integer maxHectares, List<String> features) {
        if (planRepository.existsByTier(tier)) return;
        var plan = new Plan(tier, name, price, maxDevices, maxFarms, features);
        plan.setMaxHectares(maxHectares);
        planRepository.save(plan);
    }
}
