package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.PriorityCase;

import java.util.List;

/**
 * Service interface for querying priority cases (farms with active alerts) assigned to agronomists.
 */
public interface PriorityCasesQueryService {
    /**
     * Retrieves all priority cases for a specific agronomist user.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of PriorityCase objects containing active alert and client details
     */
    List<PriorityCase> findPriorityCases(Long agronomistUserId);
}
