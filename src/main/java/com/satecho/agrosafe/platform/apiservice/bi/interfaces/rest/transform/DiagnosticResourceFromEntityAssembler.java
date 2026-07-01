package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.DiagnosticResource;

public class DiagnosticResourceFromEntityAssembler {

    private DiagnosticResourceFromEntityAssembler() {
    }

    public static DiagnosticResource toResourceFromEntity(DiagnosticSession entity) {
        return new DiagnosticResource(
                entity.getId(),
                entity.getDeviceId(),
                entity.getStatus(),
                entity.getComponentResults(),
                entity.getRecommendation(),
                entity.getStartedAt(),
                entity.getCompletedAt()
        );
    }
}
