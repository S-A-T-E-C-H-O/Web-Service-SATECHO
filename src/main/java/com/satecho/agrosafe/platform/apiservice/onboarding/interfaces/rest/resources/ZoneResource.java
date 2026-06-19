package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

public record ZoneResource(Long id, Long farmId, String name, double areaHectares, String cropType, Long deviceId, ThresholdLimitsResource thresholds) {}