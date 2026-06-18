package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events;

import java.time.Instant;

public record IrrigationStartedEvent(Long sessionId, Long zoneId, Long deviceId, String triggeredBy, Integer durationMinutes, Instant startedAt) {}
