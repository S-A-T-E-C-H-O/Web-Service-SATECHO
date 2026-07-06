package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Recommendation} aggregates.
 *
 * @author Colegio
 * @version 1.0
 */
public interface RecommendationRepository {
    /**
     * Saves a recommendation.
     *
     * @param recommendation the recommendation to save
     * @return the saved recommendation
     */
    Recommendation save(Recommendation recommendation);

    /**
     * Finds a recommendation by its unique identifier.
     *
     * @param id the unique identifier of the recommendation
     * @return an {@link Optional} containing the recommendation if found, or empty otherwise
     */
    Optional<Recommendation> findById(Long id);

    /**
     * Finds all recommendations for a given farmer, ordered by generated date in descending order.
     *
     * @param farmerUserId the unique identifier of the farmer user
     * @return a list of recommendations associated with the farmer, sorted descending by creation time
     */
    List<Recommendation> findByFarmerUserIdOrderByGeneratedAtDesc(Long farmerUserId);

    /**
     * Finds all recommendations written by a given agronomist, ordered by generated date in descending order.
     *
     * @param agronomistUserId the unique identifier of the agronomist user
     * @return a list of recommendations written by the agronomist, sorted descending by creation time
     */
    List<Recommendation> findByAgronomistUserIdOrderByGeneratedAtDesc(Long agronomistUserId);
}
