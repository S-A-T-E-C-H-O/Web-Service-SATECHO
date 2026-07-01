package com.satecho.agrosafe.platform.apiservice.bi.domain.model.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UserSuspendedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final Long userId;
    private final String reason;
    private final Long suspendedBy;
    private final Instant occurredAt;

    public UserSuspendedEvent(Long userId, String reason, Long suspendedBy) {
        this.eventId = UUID.randomUUID().toString();
        this.userId = userId;
        this.reason = reason;
        this.suspendedBy = suspendedBy;
        this.occurredAt = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    public Long getSuspendedBy() {
        return suspendedBy;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
