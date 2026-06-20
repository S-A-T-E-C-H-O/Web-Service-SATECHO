package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import java.time.Instant;

public record BatchIngestResource(Long deviceId, Long zoneId, MetricType metricType, Double value, Instant timestamp) {}
