package com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;

import java.util.List;
import java.util.Optional;

public interface AgronomistClientQueryService {
    List<AgronomistClient> findByAgronomistId(Long agronomistId);
    Optional<AgronomistClient> findByFarmerId(Long farmerId);
    int countTotalClients(Long agronomistId);
    int countActiveClients(Long agronomistId);
    long countRecommendations(Long agronomistId);
}
