package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries;

import java.time.Instant;

/** A client's active critical alert surfaced on the agronomist's priority-cases panel (EP-009-US005). */
public record PriorityCase(Long alertId, Long farmerUserId, String farmerName, Long farmId, String farmName,
                            String alertType, String severity, Instant createdAt) {
}
