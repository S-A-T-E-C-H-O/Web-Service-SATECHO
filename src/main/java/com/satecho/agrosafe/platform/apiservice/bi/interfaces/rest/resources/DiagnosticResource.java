package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

import java.time.Instant;

public record DiagnosticResource(
        Long id,
        Long deviceId,
        String status,
        String componentResults,
        String recommendation,
        Instant startedAt,
        Instant completedAt
) {
}
