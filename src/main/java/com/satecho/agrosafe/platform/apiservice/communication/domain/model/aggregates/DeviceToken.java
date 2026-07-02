package com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * A Firebase Cloud Messaging registration token for a user's mobile device,
 * used by the communication context to resolve the push recipient for a given userId.
 */
@Getter
public class DeviceToken extends AuditableAbstractAggregateRoot<DeviceToken> {
    @Setter private Long id;
    @Setter private Long userId;
    @Setter private String fcmToken;
    @Setter private String platform;
    @Setter private Instant registeredAt;
    @Setter private Instant lastSeenAt;

    public DeviceToken() {}

    public DeviceToken(Long userId, String fcmToken, String platform) {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (fcmToken == null || fcmToken.isBlank()) throw new IllegalArgumentException("fcmToken cannot be null or blank");
        this.userId = userId;
        this.fcmToken = fcmToken;
        this.platform = platform;
        this.registeredAt = Instant.now();
        this.lastSeenAt = Instant.now();
    }

    public void touch() {
        this.lastSeenAt = Instant.now();
    }
}