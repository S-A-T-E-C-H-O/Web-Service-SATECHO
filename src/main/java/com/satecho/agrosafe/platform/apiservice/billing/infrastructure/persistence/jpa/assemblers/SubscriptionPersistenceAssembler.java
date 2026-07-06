package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;

/**
 * Mapper utility to convert subscriptions between the domain model and JPA database entities.
 */
public final class SubscriptionPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of utility mapper class.
     */
    private SubscriptionPersistenceAssembler() {}

    /**
     * Converts a JPA persistence entity into a domain Subscription aggregate root.
     *
     * @param e the database entity
     * @return the initialized domain aggregate, or null if the entity is null
     */
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

    /**
     * Converts a domain Subscription aggregate root into a JPA persistence entity.
     *
     * @param d the domain aggregate
     * @return the database persistence entity, or null if the domain object is null
     */
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
