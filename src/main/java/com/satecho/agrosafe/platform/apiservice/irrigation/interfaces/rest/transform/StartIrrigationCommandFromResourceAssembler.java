package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.StartIrrigationCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.StartIrrigationResource;

public final class StartIrrigationCommandFromResourceAssembler {
    private StartIrrigationCommandFromResourceAssembler() {}

    public static StartIrrigationCommand toCommandFromResource(StartIrrigationResource resource, Long zoneId) {
        return new StartIrrigationCommand(
                resource.farmId(),
                zoneId,
                resource.deviceId(),
                resource.triggeredBy(),
                resource.durationMinutes()
        );
    }
}
