package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.NotificationPreferenceResource;

import java.util.List;

public class NotificationPreferenceResourceFromEntityAssembler {
    private NotificationPreferenceResourceFromEntityAssembler() {}
    public static NotificationPreferenceResource toResource(NotificationPreference e) {
        if (e == null) return null;
        List<String> channelNames = e.getChannelsEnabledList().stream().map(NotificationChannel::name).toList();
        return new NotificationPreferenceResource(e.getId(), e.getUserId(),
                e.getNotificationType() != null ? e.getNotificationType().name() : null,
                channelNames, e.getDailyDigestEnabled(), e.getQuietHoursStart(), e.getQuietHoursEnd());
    }
    public static List<NotificationPreferenceResource> toResourceList(List<NotificationPreference> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(NotificationPreferenceResourceFromEntityAssembler::toResource).toList();
    }
}