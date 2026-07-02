package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers;


import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;

public final class AlertPersistenceAssembler {
    private AlertPersistenceAssembler() {}

    public static Alert toDomainFromPersistence(AlertPersistenceEntity e) {
        if (e == null) return null;
        var d = new Alert();
        d.setId(e.getId());
        d.setZoneId(e.getZoneId());
        d.setDeviceId(e.getDeviceId());
        d.setFarmId(e.getFarmId());
        d.setAlertType(e.getAlertType());
        d.setSeverity(e.getSeverity());
        d.setValue(e.getValue());
        d.setThresholdValue(e.getThresholdValue());
        d.setStatus(e.getStatus());
        d.setCreatedAt(e.getAlertCreatedAt());
        d.setResolvedAt(e.getResolvedAt());
        return d;
    }

    public static AlertPersistenceEntity toPersistenceFromDomain(Alert d) {
        if (d == null) return null;
        var e = new AlertPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setZoneId(d.getZoneId());
        e.setDeviceId(d.getDeviceId());
        e.setFarmId(d.getFarmId());
        e.setAlertType(d.getAlertType());
        e.setSeverity(d.getSeverity());
        e.setValue(d.getValue());
        e.setThresholdValue(d.getThresholdValue());
        e.setStatus(d.getStatus());
        e.setAlertCreatedAt(d.getCreatedAt());
        e.setResolvedAt(d.getResolvedAt());
        return e;
    }
}