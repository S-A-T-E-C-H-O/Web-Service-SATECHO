package com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.irrigation.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface ScheduleQueryService {
    Optional<IrrigationSchedule> handle(GetScheduleByIdQuery query);
    List<IrrigationSchedule> handle(GetSchedulesByZoneQuery query);
}
