package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecurityEventRepository;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.assemblers.SecurityEventPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories.SecurityEventPersistenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import static com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories.SecurityEventPersistenceRepository.*;

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
        var spec = hasFarmId(farmId)
                .and(detectedAtGreaterThanOrEqualTo(from))
                .and(detectedAtLessThanOrEqualTo(to))
                .and(hasSeverity(severity))
                .and(hasClassification(classification));
        var pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "detectedAt"));
        return persistenceRepository.findAll(spec, pageable).stream()
                .map(SecurityEventPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
