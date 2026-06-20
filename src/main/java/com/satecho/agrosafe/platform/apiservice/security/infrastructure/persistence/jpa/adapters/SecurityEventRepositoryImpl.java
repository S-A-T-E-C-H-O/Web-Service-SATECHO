package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecurityEventRepository;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.assemblers.SecurityEventPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories.SecurityEventPersistenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class SecurityEventRepositoryImpl implements SecurityEventRepository {

    private final SecurityEventPersistenceRepository persistenceRepository;

    public SecurityEventRepositoryImpl(SecurityEventPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public SecurityEvent save(SecurityEvent event) {
        var saved = persistenceRepository.save(SecurityEventPersistenceAssembler.toPersistenceFromDomain(event));
        return SecurityEventPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<SecurityEvent> findById(Long id) {
        return persistenceRepository.findById(id).map(SecurityEventPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<SecurityEvent> findByFarmIdWithFilters(Long farmId, Instant from, Instant to,
                                                       EventSeverity severity, EventClassification classification,
                                                       int limit, int page) {
        return persistenceRepository.findByFarmIdWithFilters(farmId, from, to, severity, classification,
                PageRequest.of(page, limit)).stream().map(SecurityEventPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
