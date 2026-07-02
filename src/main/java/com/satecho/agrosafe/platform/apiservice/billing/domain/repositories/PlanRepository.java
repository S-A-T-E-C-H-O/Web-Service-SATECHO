package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

import java.util.List;
import java.util.Optional;

public interface PlanRepository {
    Plan save(Plan plan);
    List<Plan> findAll();
    Optional<Plan> findById(Long id);
    Optional<Plan> findByTier(PlanTier tier);
    boolean existsByTier(PlanTier tier);
}
