package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.ClientDetail;

import java.util.List;

/**
 * Service interface for handling query operations related to clients.
 */
public interface ClientQueryService {
    /**
     * Retrieves detailed information of all farmers assigned to a specific agronomist.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of ClientDetail containing detailed client information
     */
    List<ClientDetail> findAssignedFarmersDetailed(Long agronomistUserId);

    /**
     * Retrieves the agronomist user identifier assigned to a specific farmer.
     *
     * @param farmerUserId the identifier of the farmer user
     * @return the agronomist user identifier, or null if not found or inactive
     */
    Long findAssignedAgronomistUserId(Long farmerUserId);
}
