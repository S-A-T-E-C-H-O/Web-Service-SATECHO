package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.AnalyticsSnapshotRepository;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers.AnalyticsSnapshotPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories.AnalyticsSnapshotPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AnalyticsSnapshotRepositoryImpl implements AnalyticsSnapshotRepository {

    private final AnalyticsSnapshotPersistenceRepository persistenceRepository;

    public AnalyticsSnapshotRepositoryImpl(AnalyticsSnapshotPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public List<AnalyticsSnapshot> findByPeriodOrderByFromDateDesc(SnapshotPeriod period) {
        return persistenceRepository.findByPeriodOrderByFromDateDesc(period).stream()
                .map(AnalyticsSnapshotPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<AnalyticsSnapshot> findTopByPeriodOrderByToDateDesc(SnapshotPeriod period) {
        return persistenceRepository.findTopByPeriodOrderByToDateDesc(period)
                .map(AnalyticsSnapshotPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public AnalyticsSnapshot save(AnalyticsSnapshot snapshot) {
        var saved = persistenceRepository.save(AnalyticsSnapshotPersistenceAssembler.toPersistenceFromDomain(snapshot));
        return AnalyticsSnapshotPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
