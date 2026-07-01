package com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationQueryService {
    Optional<Recommendation> findById(Long recommendationId);
    List<Recommendation> findByFarmId(Long farmId);
    List<Recommendation> findByFarmerId(Long farmerId);
}
