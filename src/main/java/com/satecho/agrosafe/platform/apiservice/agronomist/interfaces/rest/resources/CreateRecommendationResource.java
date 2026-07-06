package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource class representing the request to create a recommendation.
 *
 * @param farmerId the unique identifier of the farmer who will receive the recommendation
 * @param zoneId the unique identifier of the zone targeted by the recommendation
 * @param title the title of the recommendation
 * @param description the description explaining the recommendation details
 * @param recommendedActions the list or text of recommended actions to take
 * @param attachmentUrl the URL to any attached documentation or image
 * @param priority the priority level of the recommendation (e.g. HIGH, MEDIUM, LOW)
 */
public record CreateRecommendationResource(Long farmerId, Long zoneId, String title, String description,
                                            String recommendedActions, String attachmentUrl, String priority) {
}

