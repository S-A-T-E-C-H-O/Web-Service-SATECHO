package com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.GenerateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface RecommendationCommandService {
    Result<Recommendation, ApplicationError> generateRecommendation(GenerateRecommendationCommand command);
    Result<Recommendation, ApplicationError> acknowledgeRecommendation(Long recommendationId, Long farmerId);
    Result<Recommendation, ApplicationError> dismissRecommendation(Long recommendationId, Long farmerId);
}
