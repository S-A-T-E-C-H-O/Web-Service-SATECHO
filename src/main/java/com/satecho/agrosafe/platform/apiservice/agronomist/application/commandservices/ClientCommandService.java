package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.AssignClientCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface ClientCommandService {
    Result<ClientAssignment, ApplicationError> assignClient(AssignClientCommand command);
}
