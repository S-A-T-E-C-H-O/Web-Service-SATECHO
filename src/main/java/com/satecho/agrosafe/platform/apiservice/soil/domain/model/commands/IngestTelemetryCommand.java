package com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import java.time.Instant;

public record IngestTelemetryCommand(Long deviceId, Long zoneId, MetricType metricType, Double value, Instant timestamp) {
    public IngestTelemetryCommand {
        if (deviceId == null) throw new IllegalArgumentException("Device ID is required");
        if (metricType == null) throw new IllegalArgumentException("Metric type is required");
        if (value == null) throw new IllegalArgumentException("Value is required");
    }
}
