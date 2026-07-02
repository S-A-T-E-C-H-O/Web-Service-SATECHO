package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.ClientDetail;

import java.util.List;

public interface ClientQueryService {
    List<ClientDetail> findAssignedFarmersDetailed(Long agronomistUserId);
    Long findAssignedAgronomistUserId(Long farmerUserId);
}
