package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource representing the detailed information of a client (farmer and farm context).
 * Field names match Mobile App's ClientDetailModel.fromJson exactly.
 *
 * @param id the unique identifier of the client association
 * @param agronomistId the unique identifier of the assigned agronomist
 * @param farmerId the unique identifier of the farmer
 * @param farmId the unique identifier of the farm
 * @param farmerName the name of the farmer
 * @param farmName the name of the farm
 * @param location the geographic location of the farm
 * @param cropType the type of crop grown on the farm
 * @param hectares the size of the farm in hectares
 * @param zoneCount the number of zones in the farm
 * @param soilHumidity the current soil humidity reading
 * @param temperature the current temperature reading
 * @param ec the electrical conductivity reading
 * @param active indicates whether the client relationship is active
 */
public record ClientDetailResource(Long id, Long agronomistId, Long farmerId, Long farmId, String farmerName,
                                    String farmName, String location, String cropType, Double hectares,
                                    Integer zoneCount, Double soilHumidity, Double temperature, Double ec,
                                    Boolean active) {
}

