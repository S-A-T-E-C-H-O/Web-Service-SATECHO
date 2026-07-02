package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

import java.time.Instant;

public record PriorityCaseResource(Long alertId, Long farmerId, String farmerName, Long farmId, String farmName,
                                    String alertType, String severity, Instant createdAt) {
}
