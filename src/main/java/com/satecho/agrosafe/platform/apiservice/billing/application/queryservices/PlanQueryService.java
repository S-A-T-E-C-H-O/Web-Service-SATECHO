package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

import java.util.List;
import java.util.Optional;

public interface PlanQueryService {
    List<Plan> findAll();
    Optional<Plan> findById(Long id);
    Optional<Plan> findByTier(PlanTier tier);
}
