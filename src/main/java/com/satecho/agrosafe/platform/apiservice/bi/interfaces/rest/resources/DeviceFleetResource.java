package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record DeviceFleetResource(
        Long id,
        String serialNumber,
        String type,
        String status,
        boolean online,
        String healthStatus,
        Double batteryLevel,
        String lastHeartbeatAt,
        String firmwareVersion
) {
}
