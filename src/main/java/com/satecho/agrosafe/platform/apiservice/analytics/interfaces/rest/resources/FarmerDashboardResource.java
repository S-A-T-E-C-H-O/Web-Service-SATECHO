package com.satecho.agrosafe.platform.apiservice.analytics.interfaces.rest.resources;

import java.util.List;

/** Field names match Mobile App's DashboardRemoteDataSource.getFarmerKpis exactly. */
public record FarmerDashboardResource(int totalZones, int onlineDevices, int offlineDevices, int errorDevices,
                                       List<FarmResource> farms, Double avgMoisture7d, Double avgEc7d,
                                       Double weeklyIrrigationHours, Integer activeAlertCount, Boolean criticalMoisture) {
    public record FarmResource(Long id, String name, String location, String cropType, double hectares) {
    }
}
