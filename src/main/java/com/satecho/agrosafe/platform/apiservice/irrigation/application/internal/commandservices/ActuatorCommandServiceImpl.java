package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.commandservices;

import com.satecho.agrosafe.platform.irrigation.application.commandservices.ActuatorCommandService;
import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.irrigation.domain.model.commands.LogActuatorCommand;
import com.satecho.agrosafe.platform.irrigation.domain.repositories.ActuatorLogRepository;
import com.satecho.agrosafe.platform.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ActuatorCommandServiceImpl implements ActuatorCommandService {

    private final ActuatorLogRepository actuatorLogRepository;

    public ActuatorCommandServiceImpl(ActuatorLogRepository actuatorLogRepository) {
        this.actuatorLogRepository = actuatorLogRepository;
    }

    @Override
    public Result<ActuatorLog, ApplicationError> logActuatorAction(LogActuatorCommand command) {
        var log = new ActuatorLog(command.deviceId(), command.zoneId(), command.actuatorType(), command.action(),
                command.commandSource(), command.success(), command.responseMessage());
        return Result.success(actuatorLogRepository.save(log));
    }
}
