package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

import java.time.Instant;

public record IrrigationScheduleResource(
        Long id,
        Long zoneId,
        Long deviceId,
        Instant startAt,
        Integer durationMinutes,
        String recurrence,
        String cronExpression,
        Boolean enabled,
        Instant nextRunAt
) {}
