package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CreateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface RecommendationCommandService {
    Result<Recommendation, ApplicationError> createRecommendation(CreateRecommendationCommand command);
    Result<Recommendation, ApplicationError> acknowledge(Long recommendationId, Long currentUserId);
    Result<Recommendation, ApplicationError> dismiss(Long recommendationId, Long currentUserId);
}
