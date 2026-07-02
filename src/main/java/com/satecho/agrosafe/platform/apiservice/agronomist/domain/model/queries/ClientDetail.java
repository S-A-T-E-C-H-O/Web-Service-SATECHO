package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries;

/** Read model for the agronomist's client roster, enriched with farm + latest telemetry. */
public record ClientDetail(Long id, Long agronomistUserId, Long farmerUserId, Long farmId, String farmerName,
                            String farmName, String location, String cropType, Double hectares, Integer zoneCount,
                            Double soilHumidity, Double temperature, Double ec, Boolean active) {
}
