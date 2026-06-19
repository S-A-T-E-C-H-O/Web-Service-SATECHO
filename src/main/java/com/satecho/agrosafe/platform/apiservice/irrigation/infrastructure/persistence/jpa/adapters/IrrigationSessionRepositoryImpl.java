package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.IrrigationSessionRepository;
import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.assemblers.IrrigationSessionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.repositories.IrrigationSessionPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class IrrigationSessionRepositoryImpl implements IrrigationSessionRepository {

    private final IrrigationSessionPersistenceRepository repository;

    @Override
    public IrrigationSession save(IrrigationSession session) {
        var entity = IrrigationSessionPersistenceAssembler.toPersistenceFromDomain(session);
        var saved = repository.save(entity);
        return IrrigationSessionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<IrrigationSession> findByZoneIdAndStatus(Long zoneId, IrrigationStatus status) {
        return repository.findByZoneIdAndStatus(zoneId, status)
                .map(IrrigationSessionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<IrrigationSession> findActiveByZoneId(Long zoneId) {
        return repository.findActiveByZoneId(zoneId)
                .map(IrrigationSessionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<IrrigationSession> findByZoneIdOrderByStartedAtDesc(Long zoneId, int limit) {
        return repository.findByZoneIdOrderByStartedAtDesc(zoneId, PageRequest.of(0, limit))
                .stream()
                .map(IrrigationSessionPersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toList());
    }

    @Override
    public List<IrrigationSession> findByZoneIdAndStartedAtBetween(Long zoneId, Instant from, Instant to, int limit) {
        return repository.findByZoneIdAndStartedAtBetween(zoneId, from, to, PageRequest.of(0, limit))
                .stream()
                .map(IrrigationSessionPersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toList());
    }
}
