package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

import java.time.Instant;

public record IrrigationSessionResource(
        Long id,
        Long zoneId,
        Long deviceId,
        String status,
        Instant startedAt,
        Instant stoppedAt,
        Integer durationMinutes,
        Double totalWaterUsedLiters,
        String triggeredBy
) {}
