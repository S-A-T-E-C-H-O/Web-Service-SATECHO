package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries;

import java.time.Instant;

public record GetActuatorLogsByDeviceQuery(Long deviceId, Instant fromDate, Instant toDate, Integer limit) {
    public GetActuatorLogsByDeviceQuery { if (limit == null || limit < 1 || limit > 100) limit = 20; }
}
