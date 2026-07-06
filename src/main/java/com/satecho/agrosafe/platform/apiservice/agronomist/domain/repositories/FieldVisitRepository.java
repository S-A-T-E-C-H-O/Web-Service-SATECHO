package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link FieldVisit} aggregates.
 *
 * @author Colegio
 * @version 1.0
 */
public interface FieldVisitRepository {
    /**
     * Saves a field visit.
     *
     * @param visit the field visit to save
     * @return the saved field visit
     */
    FieldVisit save(FieldVisit visit);

    /**
     * Finds a field visit by its unique identifier.
     *
     * @param id the unique identifier of the field visit
     * @return an {@link Optional} containing the field visit if found, or empty otherwise
     */
    Optional<FieldVisit> findById(Long id);

    /**
     * Finds all field visits for a given agronomist, ordered by scheduled date in ascending order.
     *
     * @param agronomistUserId the unique identifier of the agronomist user
     * @return a list of field visits sorted by schedule date ascending
     */
    List<FieldVisit> findByAgronomistUserIdOrderByScheduledAtAsc(Long agronomistUserId);

    /**
     * Finds all field visits for a given farm, ordered by scheduled date in descending order.
     *
     * @param farmId the unique identifier of the farm
     * @return a list of field visits sorted by schedule date descending
     */
    List<FieldVisit> findByFarmIdOrderByScheduledAtDesc(Long farmId);
}
