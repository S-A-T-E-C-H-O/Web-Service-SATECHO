package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.SubscriptionStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity mapping representing active user subscriptions in the 'subscriptions' table.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
public class SubscriptionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The ID of the subscriber user.
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    /**
     * The database key of the selected plan.
     */
    @Column(name = "plan_id", nullable = false)
    private Long planId;

    /**
     * The operational state of the subscription.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SubscriptionStatus status;

    /**
     * The billing schedule cycle.
     */
    @Column(name = "billing_cycle", length = 20)
    private String billingCycle;

    /**
     * The subscription start timestamp.
     */
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    /**
     * The upcoming billing target date.
     */
    @Column(name = "current_period_end")
    private Instant currentPeriodEnd;

    /**
     * The cancellation timestamp if cancellation occurred.
     */
    @Column(name = "cancelled_at")
    private Instant cancelledAt;
}
