package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "device_tokens", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "fcm_token"}))
@Getter
@Setter
@NoArgsConstructor
public class DeviceTokenPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "fcm_token", nullable = false, length = 512)
    private String fcmToken;
    @Column(name = "platform", length = 20)
    private String platform;
    @Column(name = "registered_at", nullable = false)
    private Instant registeredAt;
    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;
}
