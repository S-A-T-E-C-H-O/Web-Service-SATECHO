package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events;

import java.time.Instant;

public record ClientLinkedEvent(
        Long agronomistClientId,
        Long agronomistId,
        Long farmerId,
        Instant linkedAt
) {
}
