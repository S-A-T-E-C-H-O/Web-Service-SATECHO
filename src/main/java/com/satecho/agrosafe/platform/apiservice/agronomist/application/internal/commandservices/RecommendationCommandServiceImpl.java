package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.RecommendationCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CreateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.RecommendationRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecommendationCommandServiceImpl implements RecommendationCommandService {

    private final RecommendationRepository recommendationRepository;
    private final ClientAssignmentRepository clientAssignmentRepository;

    public RecommendationCommandServiceImpl(RecommendationRepository recommendationRepository,
                                              ClientAssignmentRepository clientAssignmentRepository) {
        this.recommendationRepository = recommendationRepository;
        this.clientAssignmentRepository = clientAssignmentRepository;
    }

    @Override
    public Result<Recommendation, ApplicationError> createRecommendation(CreateRecommendationCommand command) {
        if (!clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(
                command.agronomistUserId(), command.farmerUserId())) {
            return Result.failure(ApplicationError.forbidden("Recommendation",
                    "This farmer is not among your assigned clients"));
        }
        var recommendation = new Recommendation(command.agronomistUserId(), command.farmerUserId(), command.zoneId(),
                command.title(), command.description(), command.recommendedActions(), command.attachmentUrl(),
                command.priority());
        return Result.success(recommendationRepository.save(recommendation));
    }

    @Override
    public Result<Recommendation, ApplicationError> acknowledge(Long recommendationId, Long currentUserId) {
        var recommendation = recommendationRepository.findById(recommendationId);
        if (recommendation.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Recommendation", String.valueOf(recommendationId)));
        }
        var r = recommendation.get();
        if (!r.getFarmerUserId().equals(currentUserId)) {
            return Result.failure(ApplicationError.forbidden("Recommendation", "Only the addressed farmer can acknowledge this recommendation"));
        }
        r.acknowledge();
        return Result.success(recommendationRepository.save(r));
    }

    @Override
    public Result<Recommendation, ApplicationError> dismiss(Long recommendationId, Long currentUserId) {
        var recommendation = recommendationRepository.findById(recommendationId);
        if (recommendation.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Recommendation", String.valueOf(recommendationId)));
        }
        var r = recommendation.get();
        if (!r.getFarmerUserId().equals(currentUserId)) {
            return Result.failure(ApplicationError.forbidden("Recommendation", "Only the addressed farmer can dismiss this recommendation"));
        }
        r.dismiss();
        return Result.success(recommendationRepository.save(r));
    }
}
