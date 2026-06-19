package com.satecho.agrosafe.platform.apiservice.communication.domain.model.events;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

import java.time.Instant;

public record NotificationDispatchedEvent(Long notificationId, Long userId, NotificationType type, String channel, Instant sentAt) {}