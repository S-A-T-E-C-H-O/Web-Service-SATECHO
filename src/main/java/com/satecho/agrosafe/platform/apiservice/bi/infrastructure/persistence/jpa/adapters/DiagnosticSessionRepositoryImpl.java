package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.DiagnosticSessionRepository;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers.DiagnosticSessionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories.DiagnosticSessionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiagnosticSessionRepositoryImpl implements DiagnosticSessionRepository {

    private final DiagnosticSessionPersistenceRepository persistenceRepository;

    public DiagnosticSessionRepositoryImpl(DiagnosticSessionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<DiagnosticSession> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(DiagnosticSessionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<DiagnosticSession> findByDeviceIdOrderByStartedAtDesc(Long deviceId) {
        return persistenceRepository.findByDeviceIdOrderByStartedAtDesc(deviceId).stream()
                .map(DiagnosticSessionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public DiagnosticSession save(DiagnosticSession session) {
        var saved = persistenceRepository.save(DiagnosticSessionPersistenceAssembler.toPersistenceFromDomain(session));
        return DiagnosticSessionPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
