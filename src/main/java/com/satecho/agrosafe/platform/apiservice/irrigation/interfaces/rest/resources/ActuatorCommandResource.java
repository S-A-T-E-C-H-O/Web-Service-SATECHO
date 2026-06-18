package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

public record ActuatorCommandResource(
        Long deviceId,
        Long zoneId,
        String actuatorType,
        String action,
        String commandSource,
        boolean success,
        String responseMessage
) {}
