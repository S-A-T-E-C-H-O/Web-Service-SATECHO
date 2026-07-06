package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.AssignClientCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

/**
 * Service interface for handling command operations related to clients.
 */
public interface ClientCommandService {
    /**
     * Assigns a client to an agronomist.
     *
     * @param command the command containing agronomist and client details
     * @return a Result containing the ClientAssignment if successful, or an ApplicationError if not
     */
    Result<ClientAssignment, ApplicationError> assignClient(AssignClientCommand command);
}
