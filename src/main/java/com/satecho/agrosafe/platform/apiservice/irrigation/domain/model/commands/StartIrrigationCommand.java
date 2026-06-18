package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands;

public record StartIrrigationCommand(Long farmId, Long zoneId, Long deviceId, String triggeredBy, Integer durationMinutes) {
    public StartIrrigationCommand {
        if (zoneId == null) throw new IllegalArgumentException("zoneId cannot be null");
        if (deviceId == null) throw new IllegalArgumentException("deviceId cannot be null");
        if (triggeredBy == null || triggeredBy.isBlank()) throw new IllegalArgumentException("triggeredBy cannot be null or blank");
    }
}
