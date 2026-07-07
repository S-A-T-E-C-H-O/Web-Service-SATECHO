package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources;

import java.time.Instant;

public record IngestSecurityEventResource(Long farmId, Long deviceId, Long zoneId, String classification,
                                          Double confidenceLevel, Instant detectedAt,
                                          String locationDescription, String rawData,
                                          Double pulseDurationMs, Integer triggersPerMinute) {}
