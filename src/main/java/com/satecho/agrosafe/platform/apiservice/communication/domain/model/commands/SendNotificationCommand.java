package com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

public record SendNotificationCommand(Long userId, NotificationType type, String title, String body,
                                      NotificationChannel channel, Long relatedEntityId, String relatedEntityType,
                                      String recipientAddress) {
    public SendNotificationCommand {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title cannot be null or blank");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("body cannot be null or blank");
        if (channel == null) throw new IllegalArgumentException("channel cannot be null");
    }
}