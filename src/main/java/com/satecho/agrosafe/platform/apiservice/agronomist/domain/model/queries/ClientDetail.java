package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries;

/**
 * Read model for the agronomist's client roster, enriched with farm + latest telemetry.
 *
 * @param id             the assignment ID
 * @param agronomistUserId the unique identifier of the agronomist user
 * @param farmerUserId   the unique identifier of the farmer user
 * @param farmId         the unique identifier of the farm
 * @param farmerName     the name of the farmer
 * @param farmName       the name of the farm
 * @param location       the location of the farm
 * @param cropType       the type of crop grown
 * @param hectares       the size of the farm in hectares
 * @param zoneCount      the number of zones in the farm
 * @param soilHumidity   the latest soil humidity telemetry data
 * @param temperature    the latest temperature telemetry data
 * @param ec             the latest electrical conductivity (EC) telemetry data
 * @param active         flag indicating if the assignment is active
 * @author Colegio
 * @version 1.0
 */
public record ClientDetail(Long id, Long agronomistUserId, Long farmerUserId, Long farmId, String farmerName,
                            String farmName, String location, String cropType, Double hectares, Integer zoneCount,
                            Double soilHumidity, Double temperature, Double ec, Boolean active) {
}
