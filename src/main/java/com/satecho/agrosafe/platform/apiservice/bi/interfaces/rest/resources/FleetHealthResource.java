package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

import java.time.Instant;

public record FleetHealthResource(
        Long id,
        Integer totalDevices,
        Integer onlineDevices,
        Integer offlineDevices,
        Integer errorDevices,
        Integer lowBatteryDevices,
        String devicesByType,
        Double averageSignalStrength,
        Instant snapshotAt,
        Instant createdAt
) {
}
