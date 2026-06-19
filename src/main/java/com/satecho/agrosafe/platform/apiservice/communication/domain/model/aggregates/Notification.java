package com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Notification extends AuditableAbstractAggregateRoot<Notification> {
    @Setter private Long id;
    @Setter private Long userId;
    @Setter private NotificationType type;
    @Setter private String title;
    @Setter private String body;
    @Setter private NotificationChannel channel;
    @Setter private DeliveryStatus deliveryStatus;
    @Setter private Long relatedEntityId;
    @Setter private String relatedEntityType;
    @Setter private String recipientAddress;
    @Setter private Instant sentAt;
    @Setter private Instant deliveredAt;
    @Setter private Instant readAt;

    public Notification() {}

    public Notification(Long userId, NotificationType type, String title, String body,
                        NotificationChannel channel, Long relatedEntityId, String relatedEntityType) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title cannot be null or blank");
        if (body == null || body.isBlank()) throw new IllegalArgumentException("body cannot be null or blank");
        if (channel == null) throw new IllegalArgumentException("channel cannot be null");
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.body = body;
        this.channel = channel;
        this.deliveryStatus = DeliveryStatus.PENDING;
        this.relatedEntityId = relatedEntityId;
        this.relatedEntityType = relatedEntityType;
    }

    public void dispatch() {
        if (this.deliveryStatus != DeliveryStatus.PENDING) throw new IllegalStateException("Notification has already been dispatched");
        this.deliveryStatus = DeliveryStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void markAsDelivered() {
        if (this.deliveryStatus != DeliveryStatus.SENT) throw new IllegalStateException("Notification must be in SENT status");
        this.deliveryStatus = DeliveryStatus.DELIVERED;
        this.deliveredAt = Instant.now();
    }

    public void markAsFailed() {
        this.deliveryStatus = DeliveryStatus.FAILED;
    }

    public void markAsRead() {
        if (this.readAt != null) throw new IllegalStateException("Notification has already been read");
        this.readAt = Instant.now();
    }
}