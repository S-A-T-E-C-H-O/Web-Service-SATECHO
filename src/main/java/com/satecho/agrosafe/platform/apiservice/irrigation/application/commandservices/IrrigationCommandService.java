package com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.StartIrrigationCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.StopIrrigationCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface IrrigationCommandService {
    Result<IrrigationSession, ApplicationError> startIrrigation(StartIrrigationCommand command);
    Result<IrrigationSession, ApplicationError> stopIrrigation(StopIrrigationCommand command);
}
