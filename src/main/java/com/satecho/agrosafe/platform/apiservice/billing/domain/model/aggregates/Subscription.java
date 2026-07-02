package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.SubscriptionStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** A user's active subscription to a {@link Plan} (EP-012-US001/US002). */
@Getter
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    @Setter private Long id;
    @Setter private Long userId;
    @Setter private Long planId;
    @Setter private SubscriptionStatus status;
    @Setter private String billingCycle;
    @Setter private Instant startedAt;
    @Setter private Instant currentPeriodEnd;
    @Setter private Instant cancelledAt;

    public Subscription() {}

    public Subscription(Long userId, Long planId) {
        this.userId = userId;
        this.planId = planId;
        this.status = SubscriptionStatus.ACTIVE;
        this.billingCycle = "MONTHLY";
        this.startedAt = Instant.now();
        this.currentPeriodEnd = Instant.now().plusSeconds(30L * 24 * 3600);
    }

    public void changePlan(Long newPlanId) {
        this.planId = newPlanId;
        this.status = SubscriptionStatus.ACTIVE;
        this.startedAt = Instant.now();
        this.currentPeriodEnd = Instant.now().plusSeconds(30L * 24 * 3600);
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }
}
