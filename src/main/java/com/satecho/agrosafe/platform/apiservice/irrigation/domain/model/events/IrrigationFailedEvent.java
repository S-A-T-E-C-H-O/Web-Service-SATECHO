package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events;

import java.time.Instant;

public record IrrigationFailedEvent(Long sessionId, Long zoneId, Long deviceId, String reason, Instant timestamp) {}
