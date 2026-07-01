package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.CreateFirmwareCommand;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.FirmwareResource;

public class CreateFirmwareCommandFromResourceAssembler {

    private CreateFirmwareCommandFromResourceAssembler() {
    }

    public static CreateFirmwareCommand toCommandFromResource(FirmwareResource resource) {
        return new CreateFirmwareCommand(
                resource.version(),
                resource.deviceModel(),
                resource.changelog(),
                resource.binaryUrl()
        );
    }
}
