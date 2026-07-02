package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.RecommendationQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendationQueryServiceImpl implements RecommendationQueryService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationQueryServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<Recommendation> findById(Long id) {
        return recommendationRepository.findById(id);
    }

    @Override
    public List<Recommendation> findByFarmerUserId(Long farmerUserId) {
        return recommendationRepository.findByFarmerUserIdOrderByGeneratedAtDesc(farmerUserId);
    }

    @Override
    public List<Recommendation> findByAgronomistUserId(Long agronomistUserId) {
        return recommendationRepository.findByAgronomistUserIdOrderByGeneratedAtDesc(agronomistUserId);
    }
}
