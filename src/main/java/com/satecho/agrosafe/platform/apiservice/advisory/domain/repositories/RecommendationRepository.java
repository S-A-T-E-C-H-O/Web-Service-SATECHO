package com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository {
    Recommendation save(Recommendation recommendation);
    Optional<Recommendation> findById(Long id);
    List<Recommendation> findByFarmId(Long farmId);
    List<Recommendation> findByFarmerId(Long farmerId);
    long countByAgronomistId(Long agronomistId);
}
