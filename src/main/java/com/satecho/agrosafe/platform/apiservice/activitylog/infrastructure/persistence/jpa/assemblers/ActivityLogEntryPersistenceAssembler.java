package com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.aggregates.ActivityLogEntry;
import com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.entities.ActivityLogEntryPersistenceEntity;

public final class ActivityLogEntryPersistenceAssembler {
    private ActivityLogEntryPersistenceAssembler() {}

    public static ActivityLogEntry toDomainFromPersistence(ActivityLogEntryPersistenceEntity e) {
        if (e == null) return null;
        var d = new ActivityLogEntry();
        d.setId(e.getId());
        d.setFarmId(e.getFarmId());
        d.setType(e.getType());
        d.setDescription(e.getDescription());
        d.setOccurredAt(e.getOccurredAt());
        return d;
    }

    public static ActivityLogEntryPersistenceEntity toPersistenceFromDomain(ActivityLogEntry d) {
        if (d == null) return null;
        var e = new ActivityLogEntryPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setFarmId(d.getFarmId());
        e.setType(d.getType());
        e.setDescription(d.getDescription());
        e.setOccurredAt(d.getOccurredAt());
        return e;
    }
}
