package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class NotificationPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;
    @Column(nullable = false, length = 200)
    private String title;
    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationChannel channel;
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false, length = 20)
    private DeliveryStatus deliveryStatus;
    @Column(name = "related_entity_id")
    private Long relatedEntityId;
    @Column(name = "related_entity_type", length = 50)
    private String relatedEntityType;
    @Column(name = "recipient_address", length = 500)
    private String recipientAddress;
    @Column(name = "sent_at")
    private Instant sentAt;
    @Column(name = "delivered_at")
    private Instant deliveredAt;
    @Column(name = "read_at")
    private Instant readAt;
}