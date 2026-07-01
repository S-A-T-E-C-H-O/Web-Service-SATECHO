package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.FieldVisitRepository;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers.FieldVisitPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories.FieldVisitPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FieldVisitRepositoryImpl implements FieldVisitRepository {

    private final FieldVisitPersistenceRepository persistenceRepository;

    public FieldVisitRepositoryImpl(FieldVisitPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public FieldVisit save(FieldVisit visit) {
        var saved = persistenceRepository.save(FieldVisitPersistenceAssembler.toPersistenceFromDomain(visit));
        return FieldVisitPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<FieldVisit> findById(Long id) {
        return persistenceRepository.findById(id).map(FieldVisitPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<FieldVisit> findByAgronomistId(Long agronomistId) {
        return persistenceRepository.findByAgronomistId(agronomistId)
                .stream().map(FieldVisitPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
