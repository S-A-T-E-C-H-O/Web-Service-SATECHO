package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.FleetHealthPersistenceEntity;

public final class FleetHealthPersistenceAssembler {

    private FleetHealthPersistenceAssembler() {
    }

    public static FleetHealth toDomainFromPersistence(FleetHealthPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new FleetHealth();
        domain.setId(entity.getId());
        domain.setTotalDevices(entity.getTotalDevices());
        domain.setOnlineDevices(entity.getOnlineDevices());
        domain.setOfflineDevices(entity.getOfflineDevices());
        domain.setErrorDevices(entity.getErrorDevices());
        domain.setLowBatteryDevices(entity.getLowBatteryDevices());
        domain.setDevicesByType(entity.getDevicesByType());
        domain.setAverageSignalStrength(entity.getAverageSignalStrength());
        domain.setSnapshotAt(entity.getSnapshotAt());
        return domain;
    }

    public static FleetHealthPersistenceEntity toPersistenceFromDomain(FleetHealth domain) {
        if (domain == null) return null;
        var entity = new FleetHealthPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setTotalDevices(domain.getTotalDevices());
        entity.setOnlineDevices(domain.getOnlineDevices());
        entity.setOfflineDevices(domain.getOfflineDevices());
        entity.setErrorDevices(domain.getErrorDevices());
        entity.setLowBatteryDevices(domain.getLowBatteryDevices());
        entity.setDevicesByType(domain.getDevicesByType());
        entity.setAverageSignalStrength(domain.getAverageSignalStrength());
        entity.setSnapshotAt(domain.getSnapshotAt());
        return entity;
    }
}
