package com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.repositories.ActivityLogRepository;
import com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.assemblers.ActivityLogEntryPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.repositories.ActivityLogPersistenceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityLogRepositoryImpl implements ActivityLogRepository {

    private final ActivityLogPersistenceRepository persistenceRepository;

    public ActivityLogRepositoryImpl(ActivityLogPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public ActivityLogEntry save(ActivityLogEntry entry) {
        var saved = persistenceRepository.save(ActivityLogEntryPersistenceAssembler.toPersistenceFromDomain(entry));
        return ActivityLogEntryPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Page<ActivityLogEntry> findByFarmId(Long farmId, Pageable pageable) {
        return persistenceRepository.findByFarmIdOrderByOccurredAtDesc(farmId, pageable)
                .map(ActivityLogEntryPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Page<ActivityLogEntry> findByFarmIdAndType(Long farmId, ActivityType type, Pageable pageable) {
        return persistenceRepository.findByFarmIdAndTypeOrderByOccurredAtDesc(farmId, type, pageable)
                .map(ActivityLogEntryPersistenceAssembler::toDomainFromPersistence);
    }
}
