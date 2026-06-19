package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.NotificationPersistenceEntity;

public final class NotificationPersistenceAssembler {
    private NotificationPersistenceAssembler() {}

    public static Notification toDomainFromPersistence(NotificationPersistenceEntity e) {
        if (e == null) return null;
        var d = new Notification();
        d.setId(e.getId());
        d.setUserId(e.getUserId());
        d.setType(e.getType());
        d.setTitle(e.getTitle());
        d.setBody(e.getBody());
        d.setChannel(e.getChannel());
        d.setDeliveryStatus(e.getDeliveryStatus());
        d.setRelatedEntityId(e.getRelatedEntityId());
        d.setRelatedEntityType(e.getRelatedEntityType());
        d.setSentAt(e.getSentAt());
        d.setDeliveredAt(e.getDeliveredAt());
        d.setReadAt(e.getReadAt());
        return d;
    }

    public static NotificationPersistenceEntity toPersistenceFromDomain(Notification d) {
        if (d == null) return null;
        var e = new NotificationPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setUserId(d.getUserId());
        e.setType(d.getType());
        e.setTitle(d.getTitle());
        e.setBody(d.getBody());
        e.setChannel(d.getChannel());
        e.setDeliveryStatus(d.getDeliveryStatus());
        e.setRelatedEntityId(d.getRelatedEntityId());
        e.setRelatedEntityType(d.getRelatedEntityType());
        e.setSentAt(d.getSentAt());
        e.setDeliveredAt(d.getDeliveredAt());
        e.setReadAt(d.getReadAt());
        return e;
    }
}