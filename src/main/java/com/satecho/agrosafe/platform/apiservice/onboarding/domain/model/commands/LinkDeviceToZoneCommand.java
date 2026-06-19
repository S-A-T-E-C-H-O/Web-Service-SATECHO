package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

public record LinkDeviceToZoneCommand(Long zoneId, Long deviceId) {
    public LinkDeviceToZoneCommand {
        if (zoneId == null) throw new IllegalArgumentException("Zone ID is required");
        if (deviceId == null) throw new IllegalArgumentException("Device ID is required");
    }
}