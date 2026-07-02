package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanQueryServiceImpl implements PlanQueryService {

    private final PlanRepository planRepository;

    public PlanQueryServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    @Override
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public Optional<Plan> findByTier(PlanTier tier) {
        return planRepository.findByTier(tier);
    }
}
