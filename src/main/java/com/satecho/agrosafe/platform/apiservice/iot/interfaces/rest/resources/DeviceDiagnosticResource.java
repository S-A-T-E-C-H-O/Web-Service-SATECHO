package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import java.time.Instant;
import java.util.List;

public record DeviceDiagnosticResource(Long deviceId, String serialNumber, String deviceType, String status,
                                       String firmwareVersion, Double batteryLevel, String healthStatus,
                                       Boolean online, String signalStrength, Double uptimeHours,
                                       Instant lastSeen, List<ConnectionHistoryEntry> connectionHistory) {
    public record ConnectionHistoryEntry(Instant timestamp, String event, String details) {}
}
