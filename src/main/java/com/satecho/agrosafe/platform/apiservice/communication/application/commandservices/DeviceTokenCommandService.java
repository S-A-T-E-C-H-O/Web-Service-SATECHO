package com.satecho.agrosafe.platform.apiservice.communication.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.DeviceToken;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.RegisterDeviceTokenCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface DeviceTokenCommandService {
    Result<DeviceToken, ApplicationError> registerToken(RegisterDeviceTokenCommand command);
}
