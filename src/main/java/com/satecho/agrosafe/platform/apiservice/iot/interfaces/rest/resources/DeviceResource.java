package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import java.time.Instant;

public record DeviceResource(Long id, Long userId, String serialNumber, String type, String status,
                             String apiKey, String certificateThumbprint, String firmwareVersion,
                             Double batteryLevel, Instant lastHeartbeatAt, Instant lastTelemetryAt,
                             boolean online, String healthStatus) {}
