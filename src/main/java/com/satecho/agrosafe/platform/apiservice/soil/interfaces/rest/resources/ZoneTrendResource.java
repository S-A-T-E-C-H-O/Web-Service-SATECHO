package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record ZoneTrendResource(Long zoneId, String metricType, String unit, List<DataPoint> dataPoints, Double referenceMin, Double referenceMax) {
    public record DataPoint(Instant timestamp, Double value) {}
}
