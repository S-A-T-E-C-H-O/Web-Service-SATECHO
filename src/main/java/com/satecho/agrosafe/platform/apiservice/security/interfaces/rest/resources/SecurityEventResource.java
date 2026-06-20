package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources;

import java.time.Instant;

public record SecurityEventResource(Long id, Long farmId, Long deviceId, String classification,
                                    Double confidenceLevel, String severity, Instant detectedAt,
                                    Boolean acknowledged, Long acknowledgedBy, Instant acknowledgedAt,
                                    String locationDescription, String rawData) {}
