package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Plan aggregates.
 */
public interface PlanRepository {
    /**
     * Persists or updates a Plan in the database.
     *
     * @param plan the plan to save
     * @return the saved {@link Plan} aggregate
     */
    Plan save(Plan plan);

    /**
     * Retrieves all configured subscription plans.
     *
     * @return list of all {@link Plan} definitions
     */
    List<Plan> findAll();

    /**
     * Retrieves a plan using its database identifier.
     *
     * @param id the plan ID
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    Optional<Plan> findById(Long id);

    /**
     * Retrieves a plan by its subscription tier.
     *
     * @param tier the {@link PlanTier} classification
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    Optional<Plan> findByTier(PlanTier tier);

    /**
     * Checks if a plan configuration exists for the given tier.
     *
     * @param tier the {@link PlanTier} classification
     * @return true if a matching plan exists, false otherwise
     */
    boolean existsByTier(PlanTier tier);
}
