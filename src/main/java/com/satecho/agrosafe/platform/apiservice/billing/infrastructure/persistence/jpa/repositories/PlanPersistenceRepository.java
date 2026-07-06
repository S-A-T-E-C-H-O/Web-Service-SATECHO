package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.PlanPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository client for the 'plans' database table operations.
 */
@Repository
public interface PlanPersistenceRepository extends JpaRepository<PlanPersistenceEntity, Long> {
    
    /**
     * Finds a plan matching the tier key.
     *
     * @param tier the plan tier classification
     * @return an {@link Optional} containing the entity if found, or empty
     */
    Optional<PlanPersistenceEntity> findByTier(PlanTier tier);

    /**
     * Verifies if any plan matching the tier key is present.
     *
     * @param tier the plan tier classification
     * @return true if found, false otherwise
     */
    boolean existsByTier(PlanTier tier);
}
