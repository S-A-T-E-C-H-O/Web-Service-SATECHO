package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.NotificationPreferencePersistenceEntity;

public final class NotificationPreferencePersistenceAssembler {
    private NotificationPreferencePersistenceAssembler() {}

    public static NotificationPreference toDomainFromPersistence(NotificationPreferencePersistenceEntity e) {
        if (e == null) return null;
        var d = new NotificationPreference();
        d.setId(e.getId());
        d.setUserId(e.getUserId());
        d.setNotificationType(e.getNotificationType());
        d.setChannelsEnabled(e.getChannelsEnabled());
        d.setDailyDigestEnabled(e.getDailyDigestEnabled());
        d.setQuietHoursStart(e.getQuietHoursStart());
        d.setQuietHoursEnd(e.getQuietHoursEnd());
        return d;
    }

    public static NotificationPreferencePersistenceEntity toPersistenceFromDomain(NotificationPreference d) {
        if (d == null) return null;
        var e = new NotificationPreferencePersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setUserId(d.getUserId());
        e.setNotificationType(d.getNotificationType());
        e.setChannelsEnabled(d.getChannelsEnabled());
        e.setDailyDigestEnabled(d.getDailyDigestEnabled());
        e.setQuietHoursStart(d.getQuietHoursStart());
        e.setQuietHoursEnd(d.getQuietHoursEnd());
        return e;
    }
}