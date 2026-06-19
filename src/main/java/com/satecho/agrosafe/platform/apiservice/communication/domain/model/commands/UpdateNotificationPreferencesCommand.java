package com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

import java.util.List;

public record UpdateNotificationPreferencesCommand(Long userId, NotificationType notificationType,
                                                   List<NotificationChannel> channelsEnabled,
                                                   Boolean dailyDigestEnabled, String quietHoursStart,
                                                   String quietHoursEnd) {}