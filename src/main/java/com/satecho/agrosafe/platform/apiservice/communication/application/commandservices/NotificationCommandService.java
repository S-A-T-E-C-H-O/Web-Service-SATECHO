package com.satecho.agrosafe.platform.apiservice.communication.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.SendNotificationCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface NotificationCommandService {
    Result<Notification, ApplicationError> sendNotification(SendNotificationCommand command);
    Result<Notification, ApplicationError> markAsRead(Long notificationId);
}