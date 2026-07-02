package com.satecho.agrosafe.platform.apiservice.activitylog.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import org.springframework.data.domain.Page;

public interface ActivityLogQueryService {
    Page<ActivityLogEntry> findByFarm(Long farmId, ActivityType type, int page, int size);
}
