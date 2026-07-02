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

@Service
@Transactional
public class ClientCommandServiceImpl implements ClientCommandService {

    private final ClientAssignmentRepository clientAssignmentRepository;
    private final UserQueryService userQueryService;

    public ClientCommandServiceImpl(ClientAssignmentRepository clientAssignmentRepository, UserQueryService userQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.userQueryService = userQueryService;
    }

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
