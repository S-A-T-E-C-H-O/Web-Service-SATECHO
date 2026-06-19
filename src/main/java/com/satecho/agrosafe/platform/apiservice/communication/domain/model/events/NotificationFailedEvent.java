package com.satecho.agrosafe.platform.apiservice.communication.domain.model.events;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

public record NotificationFailedEvent(Long notificationId, Long userId, NotificationType type, String channel, String errorMessage) {}