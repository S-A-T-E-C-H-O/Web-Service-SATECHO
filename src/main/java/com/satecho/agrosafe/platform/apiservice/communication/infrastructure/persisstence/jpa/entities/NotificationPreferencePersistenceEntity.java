package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@NoArgsConstructor
public class NotificationPreferencePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false, length = 50)
    private NotificationType notificationType;
    @Column(name = "channels_enabled", columnDefinition = "TEXT")
    private String channelsEnabled;
    @Column(name = "daily_digest_enabled", nullable = false)
    private Boolean dailyDigestEnabled;
    @Column(name = "quiet_hours_start", length = 5)
    private String quietHoursStart;
    @Column(name = "quiet_hours_end", length = 5)
    private String quietHoursEnd;
}