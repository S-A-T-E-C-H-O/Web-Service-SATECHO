package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AgronomistClientQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AgronomistClientRepository;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.RecommendationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AgronomistClientQueryServiceImpl implements AgronomistClientQueryService {

    private final AgronomistClientRepository agronomistClientRepository;
    private final RecommendationRepository recommendationRepository;

    public AgronomistClientQueryServiceImpl(AgronomistClientRepository agronomistClientRepository,
                                            RecommendationRepository recommendationRepository) {
        this.agronomistClientRepository = agronomistClientRepository;
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public List<AgronomistClient> findByAgronomistId(Long agronomistId) {
        return agronomistClientRepository.findByAgronomistIdAndActiveTrue(agronomistId);
    }

    @Override
    public Optional<AgronomistClient> findByFarmerId(Long farmerId) {
        return agronomistClientRepository.findByFarmerIdAndActiveTrue(farmerId);
    }

    @Override
    public int countTotalClients(Long agronomistId) {
        return (int) agronomistClientRepository.countByAgronomistId(agronomistId);
    }

    @Override
    public int countActiveClients(Long agronomistId) {
        return (int) agronomistClientRepository.countByAgronomistIdAndActiveTrue(agronomistId);
    }

    @Override
    public long countRecommendations(Long agronomistId) {
        return recommendationRepository.countByAgronomistId(agronomistId);
    }
}
