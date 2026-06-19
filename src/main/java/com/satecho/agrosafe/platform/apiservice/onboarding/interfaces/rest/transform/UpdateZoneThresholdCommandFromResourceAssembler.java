package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateZoneThresholdCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.UpdateZoneThresholdResource;

public class UpdateZoneThresholdCommandFromResourceAssembler {
    private UpdateZoneThresholdCommandFromResourceAssembler() {}
    public static UpdateZoneThresholdCommand toCommandFromResource(UpdateZoneThresholdResource resource, Long zoneId) {
        var limits = new ThresholdLimits(resource.minMoisture(), resource.maxMoisture(),
                resource.minEc(), resource.maxEc(), resource.minPh(), resource.maxPh(),
                resource.minTemperature(), resource.maxTemperature());
        return new UpdateZoneThresholdCommand(zoneId, limits);
    }
}