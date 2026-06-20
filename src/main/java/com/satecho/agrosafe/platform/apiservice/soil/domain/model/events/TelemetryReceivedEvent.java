package com.satecho.agrosafe.platform.apiservice.soil.domain.model.events;

import java.time.Instant;

public record TelemetryReceivedEvent(Long readingId, Long deviceId, Long zoneId, String metricType, Double value, Instant timestamp) {}
