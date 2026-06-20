package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

public record SoilHealthResource(Long zoneId, Double soilHealthScore, String moistureStatus, String ecStatus, String phStatus, String temperatureStatus, Double waterStressIndex, String summary) {}
