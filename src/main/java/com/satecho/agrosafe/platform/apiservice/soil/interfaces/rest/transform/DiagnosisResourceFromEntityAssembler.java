package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.DiagnosisResource;

public class DiagnosisResourceFromEntityAssembler {
    private DiagnosisResourceFromEntityAssembler() {}
    public static DiagnosisResource toResourceFromEntity(Diagnosis entity) {
        return new DiagnosisResource(entity.getId(), entity.getZoneId(), entity.getWaterStressIndex(),
                entity.getSoilHealthScore(), entity.getRecommendations(), entity.getMoistureStatus(),
                entity.getEcStatus(), entity.getPhStatus(), entity.getTemperatureStatus(), entity.getGeneratedAt());
    }
}
