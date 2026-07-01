package com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class SuspendedAccount extends AuditableAbstractAggregateRoot<SuspendedAccount> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private String reason;
    @Setter private Instant suspendedAt;
    @Setter private Instant reactivatedAt;
    @Setter private Long suspendedBy;

    public SuspendedAccount() {
    }

    public SuspendedAccount(Long userId, String reason, Long suspendedBy) {
        this.userId = userId;
        this.reason = reason;
        this.suspendedBy = suspendedBy;
        this.suspendedAt = Instant.now();
    }

    public void reactivate() {
        this.reactivatedAt = Instant.now();
    }

    public boolean isActive() {
        return reactivatedAt == null;
    }
}
