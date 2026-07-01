package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.FleetHealthResource;

public class FleetHealthResourceFromEntityAssembler {

    private FleetHealthResourceFromEntityAssembler() {
    }

    public static FleetHealthResource toResourceFromEntity(FleetHealth entity) {
        return new FleetHealthResource(
                entity.getId(),
                entity.getTotalDevices(),
                entity.getOnlineDevices(),
                entity.getOfflineDevices(),
                entity.getErrorDevices(),
                entity.getLowBatteryDevices(),
                entity.getDevicesByType(),
                entity.getAverageSignalStrength(),
                entity.getSnapshotAt(),
                null
        );
    }
}
