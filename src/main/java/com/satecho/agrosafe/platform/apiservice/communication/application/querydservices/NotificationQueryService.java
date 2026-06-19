package com.satecho.agrosafe.platform.apiservice.communication.application.querydservices;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationsByUserIdQuery;

import java.util.List;
import java.util.Optional;

public interface NotificationQueryService {
    List<Notification> handle(GetNotificationsByUserIdQuery query);
    Optional<Notification> getById(Long notificationId);
}