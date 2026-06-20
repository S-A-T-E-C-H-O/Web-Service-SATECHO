package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands.ActivateDeviceCommand;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.ActivateDeviceResource;

public class ActivateDeviceCommandFromResourceAssembler {
    private ActivateDeviceCommandFromResourceAssembler() {}
    public static ActivateDeviceCommand toCommand(ActivateDeviceResource resource, Long deviceId) {
        return new ActivateDeviceCommand(deviceId, resource.certificateThumbprint());
    }
}
