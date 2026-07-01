package com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;

import java.util.List;
import java.util.Optional;

public interface AgronomistClientRepository {
    AgronomistClient save(AgronomistClient client);
    Optional<AgronomistClient> findById(Long id);
    List<AgronomistClient> findByAgronomistIdAndActiveTrue(Long agronomistId);
    Optional<AgronomistClient> findByFarmerIdAndActiveTrue(Long farmerId);
    Optional<AgronomistClient> findByAgronomistIdAndFarmerIdAndActiveTrue(Long agronomistId, Long farmerId);
    long countByAgronomistId(Long agronomistId);
    long countByAgronomistIdAndActiveTrue(Long agronomistId);
}
