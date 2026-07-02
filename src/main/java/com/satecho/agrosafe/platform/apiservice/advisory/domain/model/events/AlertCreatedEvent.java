package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;

import java.time.Instant;

public record AlertCreatedEvent(Long alertId, Long zoneId, Long deviceId, Long farmId, AlertType alertType,
                                 AlertSeverity severity, Double value, Instant createdAt) {}
