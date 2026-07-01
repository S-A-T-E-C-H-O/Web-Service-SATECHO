package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events;

import java.time.Instant;

public record RecommendationGeneratedEvent(
        Long recommendationId,
        Long farmId,
        Long agronomistId,
        Long farmerId,
        String type,
        String priority,
        Instant generatedAt
) {
}
