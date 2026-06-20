package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

import java.time.Instant;

public record SensorReadingResource(Long id, Long deviceId, Long zoneId, String metricType, Double value, String unit, Instant timestamp, Instant ingestedAt, Boolean isValid) {}
