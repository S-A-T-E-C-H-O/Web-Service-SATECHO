package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record ClientResource(
        Long id,
        Long agronomistId,
        Long farmerId,
        Instant linkedAt,
        Boolean active,
        String notes,
        Instant createdAt
) {
}
