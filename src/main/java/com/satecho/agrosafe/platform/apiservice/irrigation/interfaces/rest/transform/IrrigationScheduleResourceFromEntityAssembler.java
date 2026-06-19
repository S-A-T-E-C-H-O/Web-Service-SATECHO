package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.IrrigationScheduleResource;

public final class IrrigationScheduleResourceFromEntityAssembler {
    private IrrigationScheduleResourceFromEntityAssembler() {}

    public static IrrigationScheduleResource toResourceFromEntity(IrrigationSchedule schedule) {
        return new IrrigationScheduleResource(
                schedule.getId(),
                schedule.getZoneId(),
                schedule.getDeviceId(),
                schedule.getStartAt(),
                schedule.getDurationMinutes(),
                schedule.getRecurrence().name(),
                schedule.getCronExpression(),
                schedule.getEnabled(),
                schedule.getNextRunAt()
        );
    }
}
