package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

import java.time.Instant;

/** Field names match Mobile App's RecommendationModel.fromJson exactly. */
public record RecommendationResource(Long id, Long zoneId, Long agronomistId, String priority, String status,
                                      String title, String description, String recommendedActions,
                                      Instant generatedAt) {
}
