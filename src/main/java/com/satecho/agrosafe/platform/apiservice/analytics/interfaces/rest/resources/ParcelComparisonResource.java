package com.satecho.agrosafe.platform.apiservice.analytics.interfaces.rest.resources;

public record ParcelComparisonResource(Long zoneId, String zoneName, Double soilMoisture, Double soilTemperature,
                                        Double electricalConductivity, Double areaHectares, String cropType) {
}
