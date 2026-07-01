package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record ScheduledVisitResource(
        Long id, Long agronomistId, Long farmId,
        String farmName, String ownerName,
        Instant scheduledAt, String tag,
        String noteTitle, String noteBody,
        Boolean urgent, String status) {}
