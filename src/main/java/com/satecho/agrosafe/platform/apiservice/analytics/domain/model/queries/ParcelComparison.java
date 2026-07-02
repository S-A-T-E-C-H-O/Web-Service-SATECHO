package com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries;

public record ParcelComparison(Long zoneId, String zoneName, Double soilMoisture, Double soilTemperature,
                                Double electricalConductivity, Double areaHectares, String cropType) {
}
