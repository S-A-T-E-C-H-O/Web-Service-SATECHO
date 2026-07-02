package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands.RegisterDeviceCommand;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.RegisterDeviceResource;

public class RegisterDeviceCommandFromResourceAssembler {
    private RegisterDeviceCommandFromResourceAssembler() {}
    public static RegisterDeviceCommand toCommand(RegisterDeviceResource resource) {
        return new RegisterDeviceCommand(resource.serialNumber(), resource.type(), resource.farmerId());
    }
}
