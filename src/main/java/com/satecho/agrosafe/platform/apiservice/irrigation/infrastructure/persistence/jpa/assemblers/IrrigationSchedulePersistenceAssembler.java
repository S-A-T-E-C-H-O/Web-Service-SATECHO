package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.entities.IrrigationSchedulePersistenceEntity;

public final class IrrigationSchedulePersistenceAssembler {
    private IrrigationSchedulePersistenceAssembler() {}

    public static IrrigationSchedule toDomainFromPersistence(IrrigationSchedulePersistenceEntity e) {
        if (e == null) return null;
        var d = new IrrigationSchedule();
        d.setId(e.getId());
        d.setZoneId(e.getZoneId());
        d.setDeviceId(e.getDeviceId());
        d.setStartAt(e.getStartAt());
        d.setDurationMinutes(e.getDurationMinutes());
        d.setRecurrence(e.getRecurrence());
        d.setCronExpression(e.getCronExpression());
        d.setEnabled(e.getEnabled());
        d.setNextRunAt(e.getNextRunAt());
        return d;
    }

    public static IrrigationSchedulePersistenceEntity toPersistenceFromDomain(IrrigationSchedule d) {
        if (d == null) return null;
        var e = new IrrigationSchedulePersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setZoneId(d.getZoneId());
        e.setDeviceId(d.getDeviceId());
        e.setStartAt(d.getStartAt());
        e.setDurationMinutes(d.getDurationMinutes());
        e.setRecurrence(d.getRecurrence());
        e.setCronExpression(d.getCronExpression());
        e.setEnabled(d.getEnabled());
        e.setNextRunAt(d.getNextRunAt());
        return e;
    }
}
