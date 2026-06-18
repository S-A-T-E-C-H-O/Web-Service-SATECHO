package com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.irrigation.domain.model.commands.LogActuatorCommand;
import com.satecho.agrosafe.platform.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.shared.application.result.Result;

public interface ActuatorCommandService {
    Result<ActuatorLog, ApplicationError> logActuatorAction(LogActuatorCommand command);
}
