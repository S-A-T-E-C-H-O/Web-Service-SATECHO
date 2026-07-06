package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.RecommendationRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers.RecommendationPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories.RecommendationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link RecommendationRepository} interface.
 * <p>
 * This class provides the concrete implementation for managing agronomist recommendations by adapting
 * domain operations to a JPA persistence repository.
 * </p>
 */
@Repository
public class RecommendationRepositoryImpl implements RecommendationRepository {

    /**
     * The JPA persistence repository used for data access operations on recommendations.
     */
    private final RecommendationPersistenceRepository persistenceRepository;

    /**
     * Constructs a new {@code RecommendationRepositoryImpl} with the specified persistence repository.
     *
     * @param persistenceRepository the JPA persistence repository for recommendations
     */
    public RecommendationRepositoryImpl(RecommendationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Saves a recommendation.
     *
     * @param recommendation the recommendation domain entity to save
     * @return the saved recommendation domain entity
     */
    @Override
    public Recommendation save(Recommendation recommendation) {
        var saved = persistenceRepository.save(RecommendationPersistenceAssembler.toPersistenceFromDomain(recommendation));
        return RecommendationPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a recommendation by its ID.
     *
     * @param id the ID of the recommendation
     * @return an {@link Optional} containing the found recommendation, or empty if not found
     */
    @Override
    public Optional<Recommendation> findById(Long id) {
        return persistenceRepository.findById(id).map(RecommendationPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds recommendations for a specific farmer user, ordered by their generation date in descending order.
     *
     * @param farmerUserId the ID of the farmer user
     * @return a list of recommendations matching the farmer user ID, ordered by generation date descending
     */
    @Override
    public List<Recommendation> findByFarmerUserIdOrderByGeneratedAtDesc(Long farmerUserId) {
        return persistenceRepository.findByFarmerUserIdOrderByGeneratedAtDesc(farmerUserId).stream()
                .map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }

    /**
     * Finds recommendations for a specific agronomist user, ordered by their generation date in descending order.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @return a list of recommendations matching the agronomist user ID, ordered by generation date descending
     */
    @Override
    public List<Recommendation> findByAgronomistUserIdOrderByGeneratedAtDesc(Long agronomistUserId) {
        return persistenceRepository.findByAgronomistUserIdOrderByGeneratedAtDesc(agronomistUserId).stream()
                .map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
