package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.SessionResult;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.entities.IrrigationSessionPersistenceEntity;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.entities.SessionResultEmbeddable;

public final class IrrigationSessionPersistenceAssembler {
    private IrrigationSessionPersistenceAssembler() {}

    public static IrrigationSession toDomainFromPersistence(IrrigationSessionPersistenceEntity e) {
        if (e == null) return null;
        var d = new IrrigationSession();
        d.setId(e.getId());
        d.setFarmId(e.getFarmId());
        d.setZoneId(e.getZoneId());
        d.setDeviceId(e.getDeviceId());
        d.setStatus(e.getStatus());
        d.setStartedAt(e.getStartedAt());
        d.setStoppedAt(e.getStoppedAt());
        d.setDurationMinutes(e.getDurationMinutes());
        d.setTotalWaterUsedLiters(e.getTotalWaterUsedLiters());
        d.setTriggeredBy(e.getTriggeredBy());
        if (e.getSessionResult() != null)
            d.setSessionResult(new SessionResult(e.getSessionResult().getTotalWaterUsedLiters(), e.getSessionResult().getWaterSavedLiters(), e.getSessionResult().getDurationMinutes()));
        return d;
    }

    public static IrrigationSessionPersistenceEntity toPersistenceFromDomain(IrrigationSession d) {
        if (d == null) return null;
        var e = new IrrigationSessionPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setFarmId(d.getFarmId());
        e.setZoneId(d.getZoneId());
        e.setDeviceId(d.getDeviceId());
        e.setStatus(d.getStatus());
        e.setStartedAt(d.getStartedAt());
        e.setStoppedAt(d.getStoppedAt());
        e.setDurationMinutes(d.getDurationMinutes());
        e.setTotalWaterUsedLiters(d.getTotalWaterUsedLiters());
        e.setTriggeredBy(d.getTriggeredBy());
        if (d.getSessionResult() != null) {
            var sr = new SessionResultEmbeddable();
            sr.setTotalWaterUsedLiters(d.getSessionResult().totalWaterUsedLiters());
            sr.setWaterSavedLiters(d.getSessionResult().waterSavedLiters());
            sr.setDurationMinutes(d.getSessionResult().durationMinutes());
            e.setSessionResult(sr);
        }
        return e;
    }
}
