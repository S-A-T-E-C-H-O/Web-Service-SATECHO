package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import java.time.Instant;

public record DeviceStatusResource(Long deviceId, String serialNumber, boolean online, Instant lastHeartbeatAt,
                                   Instant lastTelemetryAt, Double batteryLevel, String healthStatus,
                                   String status, String firmwareVersion) {}
