package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record FarmerDashboardResource(
        int totalFarms,
        int totalDevices,
        long onlineDevices,
        long offlineDevices,
        long errorDevices,
        long totalZones,
        Object farms,
        Object devices
) {
}
