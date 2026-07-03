package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.SubscriptionStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity(name = "BillingSubscriptionPersistenceEntity")
@Table(name = "billing_subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    @Column(name = "plan_id", nullable = false)
    private Long planId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SubscriptionStatus status;
    @Column(name = "billing_cycle", length = 20)
    private String billingCycle;
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;
    @Column(name = "current_period_end")
    private Instant currentPeriodEnd;
    @Column(name = "cancelled_at")
    private Instant cancelledAt;
}
