package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "suspended_accounts")
@Getter
@Setter
@NoArgsConstructor
public class SuspendedAccountPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 500)
    private String reason;

    @Column(name = "suspended_at", nullable = false)
    private Instant suspendedAt;

    @Column(name = "reactivated_at")
    private Instant reactivatedAt;

    @Column(name = "suspended_by", nullable = false)
    private Long suspendedBy;
}
