package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities.SecurityEventPersistenceEntity;

public final class SecurityEventPersistenceAssembler {
    private SecurityEventPersistenceAssembler() {}

    public static SecurityEvent toDomainFromPersistence(SecurityEventPersistenceEntity e) {
        if (e == null) return null;
        var d = new SecurityEvent();
        d.setId(e.getId());
        d.setFarmId(e.getFarmId());
        d.setDeviceId(e.getDeviceId());
        d.setZoneId(e.getZoneId());
        d.setClassification(e.getClassification());
        d.setConfidenceLevel(e.getConfidenceLevel());
        d.setSeverity(e.getSeverity());
        d.setDetectedAt(e.getDetectedAt());
        d.setAcknowledged(e.getAcknowledged());
        d.setAcknowledgedBy(e.getAcknowledgedBy());
        d.setAcknowledgedAt(e.getAcknowledgedAt());
        d.setLocationDescription(e.getLocationDescription());
        d.setRawData(e.getRawData());
        d.setPulseDurationMs(e.getPulseDurationMs());
        d.setTriggersPerMinute(e.getTriggersPerMinute());
        return d;
    }

    public static SecurityEventPersistenceEntity toPersistenceFromDomain(SecurityEvent d) {
        if (d == null) return null;
        var e = new SecurityEventPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setFarmId(d.getFarmId());
        e.setDeviceId(d.getDeviceId());
        e.setZoneId(d.getZoneId());
        e.setClassification(d.getClassification());
        e.setConfidenceLevel(d.getConfidenceLevel());
        e.setSeverity(d.getSeverity());
        e.setDetectedAt(d.getDetectedAt());
        e.setAcknowledged(d.getAcknowledged());
        e.setAcknowledgedBy(d.getAcknowledgedBy());
        e.setAcknowledgedAt(d.getAcknowledgedAt());
        e.setLocationDescription(d.getLocationDescription());
        e.setRawData(d.getRawData());
        e.setPulseDurationMs(d.getPulseDurationMs());
        e.setTriggersPerMinute(d.getTriggersPerMinute());
        return e;
    }
}
