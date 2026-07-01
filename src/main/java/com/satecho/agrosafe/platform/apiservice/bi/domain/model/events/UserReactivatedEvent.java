package com.satecho.agrosafe.platform.apiservice.bi.domain.model.events;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UserReactivatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String eventId;
    private final Long userId;
    private final Long reactivatedBy;
    private final Instant occurredAt;

    public UserReactivatedEvent(Long userId, Long reactivatedBy) {
        this.eventId = UUID.randomUUID().toString();
        this.userId = userId;
        this.reactivatedBy = reactivatedBy;
        this.occurredAt = Instant.now();
    }

    public String getEventId() {
        return eventId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getReactivatedBy() {
        return reactivatedBy;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
