package com.satecho.agrosafe.platform.apiservice.security.domain.model.events;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import java.time.Instant;

public record IntrusionDetectedEvent(Long eventId, Long farmId, Long deviceId, EventClassification classification,
                                     EventSeverity severity, Double confidenceLevel, Instant detectedAt,
                                     String locationDescription) {}
