package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link PlanQueryService}.
 * Handles querying system subscription plans.
 */
@Service
public class PlanQueryServiceImpl implements PlanQueryService {

    /**
     * Repository for executing queries on plans.
     */
    private final PlanRepository planRepository;

    /**
     * Constructs a new PlanQueryServiceImpl.
     *
     * @param planRepository the plan repository
     */
    public PlanQueryServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    /**
     * Retrieves all available subscription plans.
     *
     * @return list of all {@link Plan} aggregates
     */
    @Override
    public List<Plan> findAll() {
        return planRepository.findAll();
    }

    /**
     * Finds a subscription plan by its internal ID.
     *
     * @param id the internal ID of the plan
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    @Override
    public Optional<Plan> findById(Long id) {
        return planRepository.findById(id);
    }

    /**
     * Finds a subscription plan by its tier enum value.
     *
     * @param tier the {@link PlanTier} value to search for
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    @Override
    public Optional<Plan> findByTier(PlanTier tier) {
        return planRepository.findByTier(tier);
    }
}
