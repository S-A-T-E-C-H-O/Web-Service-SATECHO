package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.BillingCycle;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Getter
public class Subscription extends AuditableAbstractAggregateRoot<Subscription> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private PlanType planType;
    @Setter private SubscriptionStatus status;
    @Setter private BillingCycle billingCycle;
    @Setter private Instant startDate;
    @Setter private Instant endDate;
    @Setter private Instant nextBillingDate;
    @Setter private Instant trialEndDate;
    @Setter private Boolean autoRenew;
    @Setter private Instant canceledAt;

    public Subscription() {
    }

    public Subscription(Long userId, PlanType planType, BillingCycle billingCycle) {
        this.userId = userId;
        this.planType = planType;
        this.billingCycle = billingCycle;
        this.status = SubscriptionStatus.TRIAL;
        this.startDate = Instant.now();
        this.trialEndDate = calculateTrialEndDate();
        this.autoRenew = true;
    }

    public static Subscription createTrial(Long userId) {
        Subscription subscription = new Subscription();
        subscription.userId = userId;
        subscription.planType = PlanType.STARTER;
        subscription.status = SubscriptionStatus.TRIAL;
        subscription.billingCycle = BillingCycle.MONTHLY;
        subscription.startDate = Instant.now();
        subscription.trialEndDate = subscription.calculateTrialEndDate();
        subscription.autoRenew = true;
        return subscription;
    }

    public void activate() {
        this.status = SubscriptionStatus.ACTIVE;
        this.startDate = Instant.now();
        this.nextBillingDate = calculateNextBillingDate();
        this.endDate = calculateEndDate();
        this.trialEndDate = null;
    }

    public void cancel() {
        this.status = SubscriptionStatus.CANCELED;
        this.canceledAt = Instant.now();
        this.autoRenew = false;
    }

    public void upgradePlan(PlanType newPlanType) {
        this.planType = newPlanType;
        this.nextBillingDate = calculateNextBillingDate();
    }

    public void updateBillingCycle(BillingCycle newBillingCycle) {
        this.billingCycle = newBillingCycle;
        this.nextBillingDate = calculateNextBillingDate();
        this.endDate = calculateEndDate();
    }

    public void markAsPastDue() { this.status = SubscriptionStatus.PAST_DUE; }
    public void markAsExpired() { this.status = SubscriptionStatus.EXPIRED; }
    public void markAsGracePeriod() { this.status = SubscriptionStatus.GRACE_PERIOD; }

    private Instant calculateTrialEndDate() {
        return Instant.now().plus(30, ChronoUnit.DAYS);
    }

    private Instant calculateNextBillingDate() {
        return switch (billingCycle) {
            case MONTHLY -> Instant.now().plus(30, ChronoUnit.DAYS);
            case QUARTERLY -> Instant.now().plus(90, ChronoUnit.DAYS);
            case ANNUAL -> Instant.now().plus(365, ChronoUnit.DAYS);
        };
    }

    private Instant calculateEndDate() {
        return switch (billingCycle) {
            case MONTHLY -> Instant.now().plus(30, ChronoUnit.DAYS);
            case QUARTERLY -> Instant.now().plus(90, ChronoUnit.DAYS);
            case ANNUAL -> Instant.now().plus(365, ChronoUnit.DAYS);
        };
    }
}
