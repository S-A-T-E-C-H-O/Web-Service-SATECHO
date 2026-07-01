package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record AgronomistDashboardResource(
        int totalFarms,
        int activeFarms,
        int totalDevices,
        long onlineDevices,
        long offlineDevices,
        long errorDevices,
        long lowBatteryDevices,
        Object farmsByCrop,
        Object devices
) {
}
