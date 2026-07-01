package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.RecommendationCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions.RecommendationNotFoundException;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.GenerateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.RecommendationGeneratedEvent;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.RecommendationRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RecommendationCommandServiceImpl implements RecommendationCommandService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationCommandServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Result<Recommendation, ApplicationError> generateRecommendation(GenerateRecommendationCommand command) {
        Recommendation recommendation = new Recommendation(
                command.farmId(),
                command.zoneId(),
                command.agronomistId(),
                command.farmerId(),
                command.type(),
                command.priority(),
                command.title(),
                command.description(),
                command.recommendedActions()
        );

        Recommendation savedRecommendation = recommendationRepository.save(recommendation);

        RecommendationGeneratedEvent event = new RecommendationGeneratedEvent(
                savedRecommendation.getId(),
                savedRecommendation.getFarmId(),
                savedRecommendation.getAgronomistId(),
                savedRecommendation.getFarmerId(),
                savedRecommendation.getType().name(),
                savedRecommendation.getPriority().name(),
                savedRecommendation.getGeneratedAt()
        );
        savedRecommendation.addDomainEvent(event);
        savedRecommendation.send();

        return Result.success(recommendationRepository.save(savedRecommendation));
    }

    @Override
    public Result<Recommendation, ApplicationError> acknowledgeRecommendation(Long recommendationId, Long farmerId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
        recommendation.acknowledge(farmerId);
        return Result.success(recommendationRepository.save(recommendation));
    }

    @Override
    public Result<Recommendation, ApplicationError> dismissRecommendation(Long recommendationId, Long farmerId) {
        Recommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new RecommendationNotFoundException(recommendationId));
        recommendation.dismiss();
        return Result.success(recommendationRepository.save(recommendation));
    }
}
