package com.satecho.agrosafe.platform.agronomist.application;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices.ClientCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.AssignClientCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientCommandServiceImpl")
class ClientCommandServiceImplTest {

    @Mock ClientAssignmentRepository clientAssignmentRepository;
    @Mock UserQueryService userQueryService;
    @InjectMocks ClientCommandServiceImpl service;

    @Test
    @DisplayName("assignClient: unknown farmer returns failure")
    void assignClient_unknownFarmer_returnsFailure() {
        when(userQueryService.handle(any(com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery.class))).thenReturn(Optional.empty());
        var result = service.assignClient(new AssignClientCommand(1L, 99L));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("assignClient: already-assigned farmer returns failure")
    void assignClient_alreadyAssigned_returnsFailure() {
        when(userQueryService.handle(any(com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery.class))).thenReturn(Optional.of(new User("f@example.com", "hash", "Farmer")));
        when(clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(1L, 2L)).thenReturn(true);

        var result = service.assignClient(new AssignClientCommand(1L, 2L));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("assignClient: valid new assignment returns success")
    void assignClient_valid_returnsSuccess() {
        when(userQueryService.handle(any(com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery.class))).thenReturn(Optional.of(new User("f@example.com", "hash", "Farmer")));
        when(clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(1L, 2L)).thenReturn(false);
        when(clientAssignmentRepository.save(any(ClientAssignment.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var result = service.assignClient(new AssignClientCommand(1L, 2L));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.toOptional().get().getFarmerUserId()).isEqualTo(2L);
    }
}
