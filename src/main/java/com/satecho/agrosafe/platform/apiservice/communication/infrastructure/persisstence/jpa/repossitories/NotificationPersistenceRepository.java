package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.NotificationPersistenceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationPersistenceRepository extends JpaRepository<NotificationPersistenceEntity, Long> {
    @Query("SELECT n FROM NotificationPersistenceEntity n WHERE n.userId = :userId " +
            "AND (:status IS NULL OR n.deliveryStatus = :status) AND (:type IS NULL OR n.type = :type) " +
            "AND (:read IS NULL OR (:read = true AND n.readAt IS NOT NULL) OR (:read = false AND n.readAt IS NULL)) " +
            "ORDER BY n.createdAt DESC")
    List<NotificationPersistenceEntity> findByUserIdWithFilters(
            @Param("userId") Long userId, @Param("status") DeliveryStatus status,
            @Param("type") NotificationType type, @Param("read") Boolean read, Pageable pageable);
}