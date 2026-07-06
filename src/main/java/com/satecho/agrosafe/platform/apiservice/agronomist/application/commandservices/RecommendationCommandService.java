package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CreateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

/**
 * Service interface for handling command operations related to recommendations.
 */
public interface RecommendationCommandService {
    /**
     * Creates a new recommendation.
     *
     * @param command the command containing recommendation details
     * @return a Result containing the created Recommendation if successful, or an ApplicationError if not
     */
    Result<Recommendation, ApplicationError> createRecommendation(CreateRecommendationCommand command);

    /**
     * Acknowledges a recommendation.
     *
     * @param recommendationId the identifier of the recommendation
     * @param currentUserId the identifier of the user acknowledging the recommendation
     * @return a Result containing the acknowledged Recommendation if successful, or an ApplicationError if not
     */
    Result<Recommendation, ApplicationError> acknowledge(Long recommendationId, Long currentUserId);

    /**
     * Dismisses a recommendation.
     *
     * @param recommendationId the identifier of the recommendation
     * @param currentUserId the identifier of the user dismissing the recommendation
     * @return a Result containing the dismissed Recommendation if successful, or an ApplicationError if not
     */
    Result<Recommendation, ApplicationError> dismiss(Long recommendationId, Long currentUserId);
}
