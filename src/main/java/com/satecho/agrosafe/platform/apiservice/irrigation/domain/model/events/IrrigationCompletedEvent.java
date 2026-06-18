package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events;

import java.time.Instant;

public record IrrigationCompletedEvent(Long sessionId, Long zoneId, Long deviceId, Double totalWaterUsedLiters, Integer durationMinutes, Instant completedAt) {}
