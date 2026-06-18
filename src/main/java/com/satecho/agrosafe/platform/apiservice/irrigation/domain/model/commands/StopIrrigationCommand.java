package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands;

public record StopIrrigationCommand(Long zoneId, String stoppedBy, Double flowRateLitersPerMinute) {
    public StopIrrigationCommand {
        if (zoneId == null) throw new IllegalArgumentException("zoneId cannot be null");
        if (flowRateLitersPerMinute == null || flowRateLitersPerMinute <= 0) flowRateLitersPerMinute = 10.0;
    }
}
