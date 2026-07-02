package com.satecho.agrosafe.platform.apiservice.activitylog.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityLogRepository {
    ActivityLogEntry save(ActivityLogEntry entry);
    Page<ActivityLogEntry> findByFarmId(Long farmId, Pageable pageable);
    Page<ActivityLogEntry> findByFarmIdAndType(Long farmId, ActivityType type, Pageable pageable);
}
