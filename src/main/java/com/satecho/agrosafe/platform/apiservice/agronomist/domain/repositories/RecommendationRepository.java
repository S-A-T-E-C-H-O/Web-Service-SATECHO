package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;

import java.util.List;
import java.util.Optional;

public interface RecommendationRepository {
    Recommendation save(Recommendation recommendation);
    Optional<Recommendation> findById(Long id);
    List<Recommendation> findByFarmerUserIdOrderByGeneratedAtDesc(Long farmerUserId);
    List<Recommendation> findByAgronomistUserIdOrderByGeneratedAtDesc(Long agronomistUserId);
}
