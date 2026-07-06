package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;

/**
 * Command record for creating a new recommendation for a farmer.
 *
 * @param agronomistUserId   the unique identifier of the agronomist user
 * @param farmerUserId       the unique identifier of the farmer user
 * @param zoneId             the unique identifier of the zone associated with this recommendation
 * @param title              the title of the recommendation
 * @param description        the detailed description of the recommendation
 * @param recommendedActions the list of actions recommended to the farmer
 * @param attachmentUrl      the URL of any attached documents or images
 * @param priority           the priority level of the recommendation
 * @author Colegio
 * @version 1.0
 */
public record CreateRecommendationCommand(Long agronomistUserId, Long farmerUserId, Long zoneId, String title,
                                           String description, String recommendedActions, String attachmentUrl,
                                           RecommendationPriority priority) {
}
