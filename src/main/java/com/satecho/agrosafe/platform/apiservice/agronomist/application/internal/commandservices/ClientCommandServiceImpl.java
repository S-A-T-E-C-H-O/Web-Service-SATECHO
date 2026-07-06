package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.ClientCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.AssignClientCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link ClientCommandService} interface.
 * Handles commands related to client assignments.
 */
@Service
@Transactional
public class ClientCommandServiceImpl implements ClientCommandService {

    /**
     * Repository for accessing client assignment data.
     */
    private final ClientAssignmentRepository clientAssignmentRepository;

    /**
     * Service for querying user information.
     */
    private final UserQueryService userQueryService;

    /**
     * Constructs a {@code ClientCommandServiceImpl} with the specified repositories and services.
     *
     * @param clientAssignmentRepository the repository for managing client assignments
     * @param userQueryService the service for querying users
     */
    public ClientCommandServiceImpl(ClientAssignmentRepository clientAssignmentRepository, UserQueryService userQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.userQueryService = userQueryService;
    }

    /**
     * Assigns a client to an agronomist.
     *
     * @param command the command containing agronomist and client details
     * @return a Result containing the ClientAssignment if successful, or an ApplicationError if not
     */
    @Override
    public Result<ClientAssignment, ApplicationError> assignClient(AssignClientCommand command) {
        if (userQueryService.handle(new GetUserByIdQuery(command.farmerUserId())).isEmpty()) {
            return Result.failure(ApplicationError.notFound("Farmer", String.valueOf(command.farmerUserId())));
        }
        if (clientAssignmentRepository.existsByAgronomistUserIdAndFarmerUserId(
                command.agronomistUserId(), command.farmerUserId())) {
            return Result.failure(ApplicationError.conflict("ClientAssignment", "Farmer is already assigned to this agronomist"));
        }
        var assignment = new ClientAssignment(command.agronomistUserId(), command.farmerUserId());
        return Result.success(clientAssignmentRepository.save(assignment));
    }
}
