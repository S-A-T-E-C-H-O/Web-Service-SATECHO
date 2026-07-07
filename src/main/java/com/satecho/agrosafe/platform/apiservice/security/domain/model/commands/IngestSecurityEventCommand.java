package com.satecho.agrosafe.platform.apiservice.security.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import java.time.Instant;

public record IngestSecurityEventCommand(Long farmId, Long deviceId, Long zoneId,
                                         EventClassification classification,
                                         Double confidenceLevel, Instant detectedAt,
                                         String locationDescription, String rawData,
                                         Double pulseDurationMs, Integer triggersPerMinute) {
    public IngestSecurityEventCommand {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        if (deviceId == null) throw new IllegalArgumentException("deviceId cannot be null");
        if (classification == null) throw new IllegalArgumentException("classification cannot be null");
        if (confidenceLevel == null) throw new IllegalArgumentException("confidenceLevel cannot be null");
        if (detectedAt == null) throw new IllegalArgumentException("detectedAt cannot be null");
    }
}
