package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record RecommendationResource(
        Long id,
        Long farmId,
        Long zoneId,
        Long agronomistId,
        Long farmerId,
        String type,
        String priority,
        String status,
        String title,
        String description,
        String recommendedActions,
        Instant generatedAt,
        Instant sentAt,
        Instant acknowledgedAt,
        Long acknowledgedBy,
        Instant createdAt
) {
}
