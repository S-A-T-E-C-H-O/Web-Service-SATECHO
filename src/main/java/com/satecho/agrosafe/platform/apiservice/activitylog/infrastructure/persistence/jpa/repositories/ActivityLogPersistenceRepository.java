package com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.entities.ActivityLogEntryPersistenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogPersistenceRepository extends JpaRepository<ActivityLogEntryPersistenceEntity, Long> {
    Page<ActivityLogEntryPersistenceEntity> findByFarmIdOrderByOccurredAtDesc(Long farmId, Pageable pageable);
    Page<ActivityLogEntryPersistenceEntity> findByFarmIdAndTypeOrderByOccurredAtDesc(Long farmId, ActivityType type, Pageable pageable);
}
