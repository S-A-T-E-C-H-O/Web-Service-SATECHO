package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.DeviceResource;

public class DeviceResourceFromEntityAssembler {

    private DeviceResourceFromEntityAssembler() {}

    public static DeviceResource toResource(Device device) {
        var creds = device.getCredentials();
        return new DeviceResource(
                device.getId(), device.getUserId(), device.getSerialNumber(),
                device.getType().name(), device.getStatus().name(),
                creds != null ? creds.apiKey() : null,
                creds != null ? creds.certificateThumbprint() : null,
                device.getFirmwareVersion(), device.getBatteryLevel(),
                device.getLastHeartbeatAt(),
                device.getLastTelemetryAt(),
                device.getOnline(), device.getHealthStatus());
    }
}
