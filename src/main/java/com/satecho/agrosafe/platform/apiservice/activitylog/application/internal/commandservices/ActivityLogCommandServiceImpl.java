package com.satecho.agrosafe.platform.apiservice.activitylog.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.activitylog.application.commandservices.ActivityLogCommandService;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.repositories.ActivityLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class ActivityLogCommandServiceImpl implements ActivityLogCommandService {

    private final ActivityLogRepository activityLogRepository;

    public ActivityLogCommandServiceImpl(ActivityLogRepository activityLogRepository) {
        this.activityLogRepository = activityLogRepository;
    }

    @Override
    public void record(Long farmId, ActivityType type, String description, Instant occurredAt) {
        if (farmId == null) return;
        activityLogRepository.save(new ActivityLogEntry(farmId, type, description, occurredAt));
    }
}
