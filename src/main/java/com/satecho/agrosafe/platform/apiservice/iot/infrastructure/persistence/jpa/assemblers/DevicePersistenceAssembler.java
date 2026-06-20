package com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceCredentials;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.entities.DeviceCredentialsEmbeddable;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;

public final class DevicePersistenceAssembler {

    private DevicePersistenceAssembler() {}

    public static Device toDomainFromPersistence(DevicePersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Device();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setSerialNumber(entity.getSerialNumber());
        domain.setType(entity.getType());
        domain.setStatus(entity.getStatus());
        if (entity.getCredentials() != null) {
            domain.setCredentials(DeviceCredentials.of(
                    entity.getCredentials().getApiKey(),
                    entity.getCredentials().getCertificateThumbprint()));
        }
        domain.setFirmwareVersion(entity.getFirmwareVersion());
        domain.setBatteryLevel(entity.getBatteryLevel());
        domain.setLastHeartbeatAt(entity.getLastHeartbeatAt());
        domain.setLastTelemetryAt(entity.getLastTelemetryAt());
        domain.setHealthStatus(entity.getHealthStatus());
        return domain;
    }

    public static DevicePersistenceEntity toPersistenceFromDomain(Device device) {
        if (device == null) return null;
        var entity = new DevicePersistenceEntity();
        if (device.getId() != null) entity.setId(device.getId());
        entity.setUserId(device.getUserId());
        entity.setSerialNumber(device.getSerialNumber());
        entity.setType(device.getType());
        entity.setStatus(device.getStatus());
        if (device.getCredentials() != null) {
            var creds = new DeviceCredentialsEmbeddable();
            creds.setApiKey(device.getCredentials().apiKey());
            creds.setCertificateThumbprint(device.getCredentials().certificateThumbprint());
            entity.setCredentials(creds);
        }
        entity.setFirmwareVersion(device.getFirmwareVersion());
        entity.setBatteryLevel(device.getBatteryLevel());
        entity.setLastHeartbeatAt(device.getLastHeartbeatAt());
        entity.setLastTelemetryAt(device.getLastTelemetryAt());
        entity.setHealthStatus(device.getHealthStatus());
        return entity;
    }
}
