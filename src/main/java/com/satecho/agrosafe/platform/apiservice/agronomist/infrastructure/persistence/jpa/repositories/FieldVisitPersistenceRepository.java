package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.FieldVisitPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for {@link FieldVisitPersistenceEntity} database operations.
 * <p>
 * This interface handles direct database queries and CRUD operations for field visits.
 * </p>
 */
@Repository
public interface FieldVisitPersistenceRepository extends JpaRepository<FieldVisitPersistenceEntity, Long> {

    /**
     * Retrieves all field visits conducted by a specific agronomist user, ordered by scheduled date in ascending order.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @return a list of field visit persistence entities
     */
    List<FieldVisitPersistenceEntity> findByAgronomistUserIdOrderByScheduledAtAsc(Long agronomistUserId);

    /**
     * Retrieves all field visits for a specific farm, ordered by scheduled date in descending order.
     *
     * @param farmId the ID of the farm
     * @return a list of field visit persistence entities
     */
    List<FieldVisitPersistenceEntity> findByFarmIdOrderByScheduledAtDesc(Long farmId);
}
