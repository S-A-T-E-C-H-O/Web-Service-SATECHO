package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/** Field names match Mobile App's ClientDetailModel.fromJson exactly. */
public record ClientDetailResource(Long id, Long agronomistId, Long farmerId, Long farmId, String farmerName,
                                    String farmName, String location, String cropType, Double hectares,
                                    Integer zoneCount, Double soilHumidity, Double temperature, Double ec,
                                    Boolean active) {
}
