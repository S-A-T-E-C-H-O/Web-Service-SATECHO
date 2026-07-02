package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;

public final class SubscriptionPersistenceAssembler {
    private SubscriptionPersistenceAssembler() {}

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity e) {
        if (e == null) return null;
        var d = new Subscription();
        d.setId(e.getId());
        d.setUserId(e.getUserId());
        d.setPlanId(e.getPlanId());
        d.setStatus(e.getStatus());
        d.setBillingCycle(e.getBillingCycle());
        d.setStartedAt(e.getStartedAt());
        d.setCurrentPeriodEnd(e.getCurrentPeriodEnd());
        d.setCancelledAt(e.getCancelledAt());
        return d;
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription d) {
        if (d == null) return null;
        var e = new SubscriptionPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setUserId(d.getUserId());
        e.setPlanId(d.getPlanId());
        e.setStatus(d.getStatus());
        e.setBillingCycle(d.getBillingCycle());
        e.setStartedAt(d.getStartedAt());
        e.setCurrentPeriodEnd(d.getCurrentPeriodEnd());
        e.setCancelledAt(d.getCancelledAt());
        return e;
    }
}
