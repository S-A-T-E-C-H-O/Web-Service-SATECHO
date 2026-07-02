package com.satecho.agrosafe.platform.agronomist.application;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices.RecommendationCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CreateRecommendationCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.RecommendationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendationCommandServiceImpl")
class RecommendationCommandServiceImplTest {

    @Mock RecommendationRepository recommendationRepository;
    @Mock ClientAssignmentRepository clientAssignmentRepository;
    @InjectMocks RecommendationCommandServiceImpl service;

    @Test
    @DisplayName("createRecommendation: farmer not assigned to this agronomist returns failure")
    void createRecommendation_notAssigned_returnsFailure() {
        when(clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(1L, 2L)).thenReturn(false);

        var result = service.createRecommendation(new CreateRecommendationCommand(1L, 2L, 5L,
                "Title", "Desc", "Actions", null, RecommendationPriority.HIGH));

        assertThat(result.isFailure()).isTrue();
        verify(recommendationRepository, never()).save(any());
    }

    @Test
    @DisplayName("createRecommendation: assigned client succeeds")
    void createRecommendation_assigned_returnsSuccess() {
        when(clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(1L, 2L)).thenReturn(true);
        when(recommendationRepository.save(any(Recommendation.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.createRecommendation(new CreateRecommendationCommand(1L, 2L, 5L,
                "Title", "Desc", "Actions", null, RecommendationPriority.HIGH));

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("acknowledge: only the addressed farmer can acknowledge")
    void acknowledge_wrongUser_returnsFailure() {
        var recommendation = new Recommendation(1L, 2L, 5L, "Title", "Desc", null, null, RecommendationPriority.MEDIUM);
        recommendation.setId(10L);
        when(recommendationRepository.findById(10L)).thenReturn(java.util.Optional.of(recommendation));

        var result = service.acknowledge(10L, 999L);

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("acknowledge: addressed farmer succeeds and marks status COMPLETED")
    void acknowledge_correctFarmer_returnsSuccess() {
        var recommendation = new Recommendation(1L, 2L, 5L, "Title", "Desc", null, null, RecommendationPriority.MEDIUM);
        recommendation.setId(10L);
        when(recommendationRepository.findById(10L)).thenReturn(java.util.Optional.of(recommendation));
        when(recommendationRepository.save(recommendation)).thenReturn(recommendation);

        var result = service.acknowledge(10L, 2L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(recommendation.getStatus().name()).isEqualTo("COMPLETED");
    }
}
