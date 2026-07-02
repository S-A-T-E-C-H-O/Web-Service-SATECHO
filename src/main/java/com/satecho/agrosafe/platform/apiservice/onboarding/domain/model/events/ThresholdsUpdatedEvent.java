package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events;

import java.time.Instant;

public record ThresholdsUpdatedEvent(Long zoneId, Long farmId, Instant updatedAt) {
}
