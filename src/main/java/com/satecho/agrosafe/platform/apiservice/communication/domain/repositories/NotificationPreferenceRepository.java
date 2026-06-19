package com.satecho.agrosafe.platform.apiservice.communication.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationPreferenceRepository {
    NotificationPreference save(NotificationPreference preference);
    List<NotificationPreference> findByUserId(Long userId);
    Optional<NotificationPreference> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}