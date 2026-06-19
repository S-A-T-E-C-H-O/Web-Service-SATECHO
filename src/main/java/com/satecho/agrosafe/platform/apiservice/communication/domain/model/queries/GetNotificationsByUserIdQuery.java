package com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

public record GetNotificationsByUserIdQuery(Long userId, Integer limit, DeliveryStatus status, NotificationType type, Boolean read) {
    public GetNotificationsByUserIdQuery {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (limit == null || limit < 1) limit = 20;
        if (limit > 100) limit = 100;
    }

    public GetNotificationsByUserIdQuery(Long userId, Integer limit, DeliveryStatus status, NotificationType type) {
        this(userId, limit, status, type, null);
    }
}