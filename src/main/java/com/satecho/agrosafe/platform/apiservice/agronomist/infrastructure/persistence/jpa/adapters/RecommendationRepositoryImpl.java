package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.RecommendationRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers.RecommendationPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories.RecommendationPersistenceRepository;
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
    public List<Recommendation> findByFarmerUserIdOrderByGeneratedAtDesc(Long farmerUserId) {
        return persistenceRepository.findByFarmerUserIdOrderByGeneratedAtDesc(farmerUserId).stream()
                .map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Recommendation> findByAgronomistUserIdOrderByGeneratedAtDesc(Long agronomistUserId) {
        return persistenceRepository.findByAgronomistUserIdOrderByGeneratedAtDesc(agronomistUserId).stream()
                .map(RecommendationPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
