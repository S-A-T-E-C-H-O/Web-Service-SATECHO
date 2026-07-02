package com.satecho.agrosafe.platform.apiservice.iot.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.RegisterDeviceResource;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

import java.util.List;

public interface DeviceCommandService {
    Result<Device, ApplicationError> handle(RegisterDeviceCommand command);
    Result<Device, ApplicationError> handle(ActivateDeviceCommand command);
    Result<Device, ApplicationError> handle(UpdateFirmwareCommand command);
    Result<Void, ApplicationError> handleDeactivate(Long deviceId);
    Result<Void, ApplicationError> handleDecommission(Long deviceId);
    Result<Device, ApplicationError> recordHeartbeat(Long deviceId, Double batteryLevel);
    Result<BatchRegisterResult, ApplicationError> handleBatchRegister(List<RegisterDeviceResource> resources);
}
