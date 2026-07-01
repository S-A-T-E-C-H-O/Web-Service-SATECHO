package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.DeviceFleetResource;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DeviceFleetResourceFromEntityAssembler {

    private static final SimpleDateFormat ISO_FORMAT = createIsoFormat();

    private DeviceFleetResourceFromEntityAssembler() {
    }

    public static DeviceFleetResource toResourceFromEntity(Device entity) {
        return new DeviceFleetResource(
                entity.getId(),
                entity.getSerialNumber(),
                entity.getType().name(),
                entity.getStatus().name(),
                entity.isWithinHeartbeatWindow(),
                entity.getHealthStatus(),
                entity.getBatteryLevel(),
                entity.getLastHeartbeatAt() != null
                        ? ISO_FORMAT.format(entity.getLastHeartbeatAt()) : null,
                entity.getFirmwareVersion()
        );
    }

    private static SimpleDateFormat createIsoFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf;
    }
}
