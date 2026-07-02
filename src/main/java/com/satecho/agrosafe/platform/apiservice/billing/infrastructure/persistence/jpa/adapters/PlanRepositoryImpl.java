package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.PlanPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.PlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlanRepositoryImpl implements PlanRepository {

    private final PlanPersistenceRepository persistenceRepository;

    public PlanRepositoryImpl(PlanPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Plan save(Plan plan) {
        var saved = persistenceRepository.save(PlanPersistenceAssembler.toPersistenceFromDomain(plan));
        return PlanPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<Plan> findAll() {
        return persistenceRepository.findAll().stream().map(PlanPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<Plan> findById(Long id) {
        return persistenceRepository.findById(id).map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Plan> findByTier(PlanTier tier) {
        return persistenceRepository.findByTier(tier).map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByTier(PlanTier tier) {
        return persistenceRepository.existsByTier(tier);
    }
}
