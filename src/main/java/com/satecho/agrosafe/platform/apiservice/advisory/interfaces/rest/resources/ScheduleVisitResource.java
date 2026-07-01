package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record ScheduleVisitResource(
        Long farmId, Instant scheduledAt,
        String tag, String noteTitle, String noteBody, Boolean urgent) {}
