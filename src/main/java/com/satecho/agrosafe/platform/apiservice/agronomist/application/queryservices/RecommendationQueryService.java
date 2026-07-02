package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationQueryService {
    Optional<Recommendation> findById(Long id);
    List<Recommendation> findByFarmerUserId(Long farmerUserId);
    List<Recommendation> findByAgronomistUserId(Long agronomistUserId);
}
