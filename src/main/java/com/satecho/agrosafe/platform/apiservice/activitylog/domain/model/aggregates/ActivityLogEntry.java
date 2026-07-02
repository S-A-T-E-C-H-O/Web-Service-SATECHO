package com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** EP-004-US020: a chronological record of what happened on a farm. */
@Getter
public class ActivityLogEntry extends AuditableAbstractAggregateRoot<ActivityLogEntry> {
    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private ActivityType type;
    @Setter private String description;
    @Setter private Instant occurredAt;

    public ActivityLogEntry() {}

    public ActivityLogEntry(Long farmId, ActivityType type, String description, Instant occurredAt) {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        this.farmId = farmId;
        this.type = type;
        this.description = description;
        this.occurredAt = occurredAt != null ? occurredAt : Instant.now();
    }
}
