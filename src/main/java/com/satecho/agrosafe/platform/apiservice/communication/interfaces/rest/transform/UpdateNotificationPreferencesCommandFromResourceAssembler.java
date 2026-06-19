package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.UpdateNotificationPreferencesCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.UpdatePreferencesResource;

import java.util.ArrayList;
import java.util.List;

public class UpdateNotificationPreferencesCommandFromResourceAssembler {
    private UpdateNotificationPreferencesCommandFromResourceAssembler() {}
    public static UpdateNotificationPreferencesCommand toCommand(Long userId, UpdatePreferencesResource resource) {
        NotificationType notificationType = NotificationType.valueOf(resource.notificationType());
        List<NotificationChannel> channels = new ArrayList<>();
        if (resource.channelsEnabled() != null) {
            for (String channelName : resource.channelsEnabled()) {
                try { channels.add(NotificationChannel.valueOf(channelName.toUpperCase())); } catch (IllegalArgumentException ignored) {}
            }
        }
        return new UpdateNotificationPreferencesCommand(userId, notificationType, channels,
                resource.dailyDigestEnabled(), resource.quietHoursStart(), resource.quietHoursEnd());
    }
}