package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

import java.time.Instant;

/**
 * Resource representing a priority case/alert that requires attention.
 *
 * @param alertId the unique identifier of the alert
 * @param farmerId the unique identifier of the farmer associated with the case
 * @param farmerName the name of the farmer
 * @param farmId the unique identifier of the farm
 * @param farmName the name of the farm
 * @param alertType the type of alert (e.g. soil humidity, temperature alert)
 * @param severity the severity level of the alert
 * @param createdAt the date and time when the alert was generated
 */
public record PriorityCaseResource(Long alertId, Long farmerId, String farmerName, Long farmId, String farmName,
                                    String alertType, String severity, Instant createdAt) {
}

