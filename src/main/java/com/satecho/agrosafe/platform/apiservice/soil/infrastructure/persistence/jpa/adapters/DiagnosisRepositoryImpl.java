package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.DiagnosisRepository;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.assemblers.DiagnosisPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.repositories.DiagnosisPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DiagnosisRepositoryImpl implements DiagnosisRepository {

    private final DiagnosisPersistenceRepository persistenceRepository;

    public DiagnosisRepositoryImpl(DiagnosisPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override public Diagnosis save(Diagnosis diagnosis) {
        var saved = persistenceRepository.save(DiagnosisPersistenceAssembler.toPersistenceFromDomain(diagnosis));
        return DiagnosisPersistenceAssembler.toDomainFromPersistence(saved);
    }
    @Override public Optional<Diagnosis> findById(Long id) {
        return persistenceRepository.findById(id).map(DiagnosisPersistenceAssembler::toDomainFromPersistence);
    }
    @Override public Optional<Diagnosis> findTopByZoneIdOrderByGeneratedAtDesc(Long zoneId) {
        return persistenceRepository.findTopByZoneIdOrderByGeneratedAtDesc(zoneId).map(DiagnosisPersistenceAssembler::toDomainFromPersistence);
    }
}
