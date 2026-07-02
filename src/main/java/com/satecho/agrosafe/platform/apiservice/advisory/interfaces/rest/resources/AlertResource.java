package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record AlertResource(Long id, Long zoneId, Long deviceId, Long farmId, String alertType, String severity,
                             Double value, Double thresholdValue, String status, Instant createdAt, Instant resolvedAt) {}
