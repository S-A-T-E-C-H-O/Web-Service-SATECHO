package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

import java.time.Instant;

public record UpdateScheduleResource(
        Instant startAt,
        Integer durationMinutes,
        String recurrence,
        String cronExpression,
        Boolean enabled
) {}
