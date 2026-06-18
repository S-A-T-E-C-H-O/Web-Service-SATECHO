package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.irrigation.interfaces.rest.resources.IrrigationSessionResource;

public final class IrrigationSessionResourceFromEntityAssembler {
    private IrrigationSessionResourceFromEntityAssembler() {}

    public static IrrigationSessionResource toResourceFromEntity(IrrigationSession session) {
        return new IrrigationSessionResource(
                session.getId(),
                session.getZoneId(),
                session.getDeviceId(),
                session.getStatus().name(),
                session.getStartedAt(),
                session.getStoppedAt(),
                session.getDurationMinutes(),
                session.getTotalWaterUsedLiters(),
                session.getTriggeredBy()
        );
    }
}
