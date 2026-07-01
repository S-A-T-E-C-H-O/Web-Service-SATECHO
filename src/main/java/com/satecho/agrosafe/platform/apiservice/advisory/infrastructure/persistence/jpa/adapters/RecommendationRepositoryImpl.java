package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.RecommendationRepository;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers.RecommendationPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories.RecommendationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecommendationRepositoryImpl implements RecommendationRepository {

    private final RecommendationPersistenceRepository persistenceRepository;

    public RecommendationRepositoryImpl(RecommendationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Recommendation save(Recommendation recommendation) {
        var saved = persistenceRepository.save(RecommendationPersistenceAssembler.toPersistenceFromDomain(recommendation));
        return RecommendationPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Recommendation> findById(Long id) {
        return persistenceRepository.findById(id).map(RecommendationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Recommendation> findByFarmId(Long farmId) {
        return persistenceRepository.findByFarmId(farmId)
                .stream().map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Recommendation> findByFarmerId(Long farmerId) {
        return persistenceRepository.findByFarmerId(farmerId)
                .stream().map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public long countByAgronomistId(Long agronomistId) {
        return persistenceRepository.countByAgronomistId(agronomistId);
    }
}
