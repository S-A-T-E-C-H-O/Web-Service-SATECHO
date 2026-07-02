package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.DeviceToken;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.DeviceTokenPersistenceEntity;

public final class DeviceTokenPersistenceAssembler {
    private DeviceTokenPersistenceAssembler() {}

    public static DeviceToken toDomainFromPersistence(DeviceTokenPersistenceEntity e) {
        if (e == null) return null;
        var d = new DeviceToken();
        d.setId(e.getId());
        d.setUserId(e.getUserId());
        d.setFcmToken(e.getFcmToken());
        d.setPlatform(e.getPlatform());
        d.setRegisteredAt(e.getRegisteredAt());
        d.setLastSeenAt(e.getLastSeenAt());
        return d;
    }

    public static DeviceTokenPersistenceEntity toPersistenceFromDomain(DeviceToken d) {
        if (d == null) return null;
        var e = new DeviceTokenPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setUserId(d.getUserId());
        e.setFcmToken(d.getFcmToken());
        e.setPlatform(d.getPlatform());
        e.setRegisteredAt(d.getRegisteredAt());
        e.setLastSeenAt(d.getLastSeenAt());
        return e;
    }
}
