package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.RecommendationQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RecommendationQueryServiceImpl implements
        RecommendationQueryService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationQueryServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Optional<Recommendation> findById(Long recommendationId) {
        return recommendationRepository.findById(recommendationId);
    }

    @Override
    public List<Recommendation> findByFarmId(Long farmId) {
        return recommendationRepository.findByFarmId(farmId);
    }

    @Override
    public List<Recommendation> findByFarmerId(Long farmerId) {
        return recommendationRepository.findByFarmerId(farmerId);
    }
}
