package com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries;

import java.util.List;

public record FarmerDashboard(int totalZones, int onlineDevices, int offlineDevices, int errorDevices,
                               List<FarmSummary> farms, Double avgMoisture7d, Double avgEc7d,
                               Double weeklyIrrigationHours, Integer activeAlertCount, Boolean criticalMoisture) {
    public record FarmSummary(Long id, String name, String location, String cropType, double hectares) {
    }
}
