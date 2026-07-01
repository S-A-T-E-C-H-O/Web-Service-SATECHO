package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

import java.time.Instant;

public record ClientDetailResource(
        Long id, Long agronomistId, Long farmerId, Long farmId,
        String farmerName, String farmName, String location,
        String cropType, double hectares, int zoneCount,
        Double soilHumidity, Double temperature, Double ec,
        Boolean active, Instant linkedAt) {}
