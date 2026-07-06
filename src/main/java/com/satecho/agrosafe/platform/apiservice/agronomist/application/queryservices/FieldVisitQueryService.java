package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;

import java.util.List;

/**
 * Service interface for handling query operations related to field visits.
 */
public interface FieldVisitQueryService {
    /**
     * Retrieves all field visits assigned to a specific agronomist.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of FieldVisit aggregates
     */
    List<FieldVisit> findByAgronomistUserId(Long agronomistUserId);

    /**
     * Retrieves all field visits for a specific farm.
     *
     * @param farmId the identifier of the farm
     * @return a List of FieldVisit aggregates
     */
    List<FieldVisit> findByFarmId(Long farmId);
}
