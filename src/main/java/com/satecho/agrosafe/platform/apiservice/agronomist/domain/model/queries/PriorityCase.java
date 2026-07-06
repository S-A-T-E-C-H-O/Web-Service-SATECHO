package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries;

import java.time.Instant;

/**
 * A client's active critical alert surfaced on the agronomist's priority-cases panel (EP-009-US005).
 *
 * @param alertId      the unique identifier of the alert
 * @param farmerUserId the unique identifier of the farmer user
 * @param farmerName   the name of the farmer
 * @param farmId       the unique identifier of the farm
 * @param farmName     the name of the farm
 * @param alertType    the type of critical alert
 * @param severity     the severity of the alert
 * @param createdAt    the timestamp when the alert was created
 * @author Colegio
 * @version 1.0
 */
public record PriorityCase(Long alertId, Long farmerUserId, String farmerName, Long farmId, String farmName,
                            String alertType, String severity, Instant createdAt) {
}
