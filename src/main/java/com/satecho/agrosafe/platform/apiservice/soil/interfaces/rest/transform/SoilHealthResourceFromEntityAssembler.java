package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.SoilHealthResource;

public class SoilHealthResourceFromEntityAssembler {
    private SoilHealthResourceFromEntityAssembler() {}
    public static SoilHealthResource toResourceFromEntity(Diagnosis entity) {
        String summary = String.format("Soil Health Score: %.0f/100. Water Stress: %.1f. Moisture: %s, EC: %s, pH: %s, Temp: %s.",
                entity.getSoilHealthScore(), entity.getWaterStressIndex(),
                entity.getMoistureStatus(), entity.getEcStatus(), entity.getPhStatus(), entity.getTemperatureStatus());
        return new SoilHealthResource(entity.getZoneId(), entity.getSoilHealthScore(),
                entity.getMoistureStatus(), entity.getEcStatus(), entity.getPhStatus(),
                entity.getTemperatureStatus(), entity.getWaterStressIndex(), summary);
    }
}
