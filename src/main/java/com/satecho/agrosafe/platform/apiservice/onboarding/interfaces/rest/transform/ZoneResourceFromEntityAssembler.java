package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.ThresholdLimitsResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources.ZoneResource;

public class ZoneResourceFromEntityAssembler {
    private ZoneResourceFromEntityAssembler() {}
    public static ZoneResource toResourceFromEntity(IrrigationZone entity) {
        ThresholdLimitsResource thresholds = null;
        if (entity.getThresholds() != null) {
            thresholds = new ThresholdLimitsResource(
                    entity.getThresholds().minMoisture(), entity.getThresholds().maxMoisture(),
                    entity.getThresholds().minEc(), entity.getThresholds().maxEc(),
                    entity.getThresholds().minPh(), entity.getThresholds().maxPh(),
                    entity.getThresholds().minTemperature(), entity.getThresholds().maxTemperature());
        }
        return new ZoneResource(entity.getId(), entity.getFarmId(), entity.getName(),
                entity.getAreaHectares(), entity.getCropType().name(), entity.getDeviceId(), thresholds);
    }
}