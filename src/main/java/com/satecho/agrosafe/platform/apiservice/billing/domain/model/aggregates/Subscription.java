package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.SubscriptionStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Represents a user's active subscription mapping to a specific plan (EP-012-US001/US002).
 */
@Getter
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {
    /**
     * Unique identifier of the subscription.
     */
    @Setter private Long id;

    /**
     * The ID of the subscriber user.
     */
    @Setter private Long userId;

    /**
     * The ID of the selected plan.
     */
    @Setter private Long planId;

    /**
     * The current operational status of the subscription (e.g. ACTIVE, CANCELLED).
     */
    @Setter private SubscriptionStatus status;

    /**
     * The cycle frequency for billing (default is MONTHLY).
     */
    @Setter private String billingCycle;

    /**
     * The timestamp when this subscription cycle started.
     */
    @Setter private Instant startedAt;

    /**
     * The timestamp indicating the end of the current billing cycle.
     */
    @Setter private Instant currentPeriodEnd;

    /**
     * The timestamp when this subscription was cancelled (if applicable).
     */
    @Setter private Instant cancelledAt;

    /**
     * Empty constructor for JPA framework use.
     */
    public Subscription() {}

    /**
     * Constructs a new active subscription for a user and plan.
     * Defaults to an ACTIVE status, MONTHLY billing cycle, and a 30-day period.
     *
     * @param userId the ID of the user
     * @param planId the ID of the plan
     */
    public Subscription(Long userId, Long planId) {
        this.userId = userId;
        this.planId = planId;
        this.status = SubscriptionStatus.ACTIVE;
        this.billingCycle = "MONTHLY";
        this.startedAt = Instant.now();
        this.currentPeriodEnd = Instant.now().plusSeconds(30L * 24 * 3600);
    }

    /**
     * Updates the subscription to target a new plan and resets the billing cycle.
     *
     * @param newPlanId the ID of the new plan
     */
    public void changePlan(Long newPlanId) {
        this.planId = newPlanId;
        this.status = SubscriptionStatus.ACTIVE;
        this.startedAt = Instant.now();
        this.currentPeriodEnd = Instant.now().plusSeconds(30L * 24 * 3600);
    }

    /**
     * Cancels the subscription and records the cancellation timestamp.
     */
    public void cancel() {
        this.status = SubscriptionStatus.CANCELLED;
        this.cancelledAt = Instant.now();
    }
}
