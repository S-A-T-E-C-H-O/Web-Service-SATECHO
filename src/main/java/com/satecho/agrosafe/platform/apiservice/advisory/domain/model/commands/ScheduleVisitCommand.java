package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands;

import java.time.Instant;

public record ScheduleVisitCommand(
        Long agronomistId, Long farmId, Instant scheduledAt,
        String tag, String noteTitle, String noteBody, Boolean urgent) {}
