package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

import java.util.List;
import java.util.Optional;

/**
 * Query service interface for retrieving plan definitions.
 */
public interface PlanQueryService {
    /**
     * Retrieves all plans defined in the system.
     *
     * @return a list of {@link Plan} aggregates
     */
    List<Plan> findAll();

    /**
     * Finds a plan by its internal ID.
     *
     * @param id the internal ID of the plan
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    Optional<Plan> findById(Long id);

    /**
     * Finds a plan by its tier name.
     *
     * @param tier the {@link PlanTier} to search for
     * @return an {@link Optional} containing the {@link Plan} if found, or empty
     */
    Optional<Plan> findByTier(PlanTier tier);
}
