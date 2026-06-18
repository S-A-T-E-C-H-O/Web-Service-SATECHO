package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events;

import java.time.Instant;

public record IrrigationStoppedEvent(Long sessionId, Long zoneId, Double totalWaterUsedLiters, Integer durationMinutes, Instant stoppedAt) {}
