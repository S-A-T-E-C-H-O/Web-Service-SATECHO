package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

import java.time.Instant;

/**
 * Resource representing the details of an agronomic recommendation.
 * Field names match Mobile App's RecommendationModel.fromJson exactly.
 *
 * @param id the unique identifier of the recommendation
 * @param zoneId the unique identifier of the zone targeted by the recommendation
 * @param agronomistId the unique identifier of the agronomist who created the recommendation
 * @param priority the priority level of the recommendation
 * @param status the status of the recommendation
 * @param title the title of the recommendation
 * @param description the description explaining the recommendation details
 * @param recommendedActions the list or text of recommended actions to take
 * @param generatedAt the timestamp when the recommendation was generated
 */
public record RecommendationResource(Long id, Long zoneId, Long agronomistId, String priority, String status,
                                      String title, String description, String recommendedActions,
                                      Instant generatedAt) {
}

