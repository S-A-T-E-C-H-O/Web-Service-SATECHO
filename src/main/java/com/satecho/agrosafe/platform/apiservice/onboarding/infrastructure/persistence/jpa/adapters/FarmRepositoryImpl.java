package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers.FarmPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories.FarmPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FarmRepositoryImpl implements FarmRepository {

    private final FarmPersistenceRepository farmPersistenceRepository;

    public FarmRepositoryImpl(FarmPersistenceRepository farmPersistenceRepository) {
        this.farmPersistenceRepository = farmPersistenceRepository;
    }

    @Override
    public Optional<Farm> findById(Long id) {
        return farmPersistenceRepository.findById(id).map(FarmPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Farm> findAllByUserId(Long userId) {
        return farmPersistenceRepository.findAllByUserId(userId).stream()
                .map(FarmPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Farm> findAll() {
        return farmPersistenceRepository.findAll().stream()
                .map(FarmPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Farm save(Farm farm) {
        var saved = farmPersistenceRepository.save(FarmPersistenceAssembler.toPersistenceFromDomain(farm));
        return FarmPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        farmPersistenceRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return farmPersistenceRepository.existsById(id);
    }
}