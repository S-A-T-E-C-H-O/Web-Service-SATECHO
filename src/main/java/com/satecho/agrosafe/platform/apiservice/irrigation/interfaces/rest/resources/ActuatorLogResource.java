package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

import java.time.Instant;

public record ActuatorLogResource(
        Long id,
        Long deviceId,
        Long zoneId,
        String actuatorType,
        String action,
        String commandSource,
        Instant executedAt,
        boolean success,
        String responseMessage
) {}
