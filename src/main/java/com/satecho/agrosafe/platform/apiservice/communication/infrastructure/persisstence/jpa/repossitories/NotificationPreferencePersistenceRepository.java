package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.NotificationPreferencePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationPreferencePersistenceRepository extends JpaRepository<NotificationPreferencePersistenceEntity, Long> {
    List<NotificationPreferencePersistenceEntity> findByUserId(Long userId);
    Optional<NotificationPreferencePersistenceEntity> findByUserIdAndNotificationType(Long userId, NotificationType notificationType);
}