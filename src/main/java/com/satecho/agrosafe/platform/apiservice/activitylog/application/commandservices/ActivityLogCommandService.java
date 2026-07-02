package com.satecho.agrosafe.platform.apiservice.activitylog.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;

import java.time.Instant;

public interface ActivityLogCommandService {
    void record(Long farmId, ActivityType type, String description, Instant occurredAt);
}
