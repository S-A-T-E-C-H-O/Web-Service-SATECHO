package com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetActuatorLogsByDeviceQuery;

import java.util.List;

public interface ActuatorQueryService {
    List<ActuatorLog> handle(GetActuatorLogsByDeviceQuery query);
}
