package com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources;

public record StartIrrigationResource(
        Long farmId,
        Long deviceId,
        String triggeredBy,
        Integer durationMinutes
) {}
