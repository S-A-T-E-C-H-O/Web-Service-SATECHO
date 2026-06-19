package com.satecho.agrosafe.platform.apiservice.communication.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class NotificationNotFoundException extends AgroSafeException {
    public NotificationNotFoundException(Long notificationId) { super("Notification not found with id: " + notificationId); }
}