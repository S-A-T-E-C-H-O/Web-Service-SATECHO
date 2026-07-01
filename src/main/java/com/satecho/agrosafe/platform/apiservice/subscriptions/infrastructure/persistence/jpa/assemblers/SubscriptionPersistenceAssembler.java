package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;

public final class SubscriptionPersistenceAssembler {

    private SubscriptionPersistenceAssembler() {
    }

    public static Subscription toDomainFromPersistence(SubscriptionPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Subscription();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setPlanType(entity.getPlanType());
        domain.setStatus(entity.getStatus());
        domain.setBillingCycle(entity.getBillingCycle());
        domain.setStartDate(entity.getStartDate());
        domain.setEndDate(entity.getEndDate());
        domain.setNextBillingDate(entity.getNextBillingDate());
        domain.setTrialEndDate(entity.getTrialEndDate());
        domain.setAutoRenew(entity.getAutoRenew());
        domain.setCanceledAt(entity.getCanceledAt());
        return domain;
    }

    public static SubscriptionPersistenceEntity toPersistenceFromDomain(Subscription domain) {
        if (domain == null) return null;
        var entity = new SubscriptionPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUserId(domain.getUserId());
        entity.setPlanType(domain.getPlanType());
        entity.setStatus(domain.getStatus());
        entity.setBillingCycle(domain.getBillingCycle());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setNextBillingDate(domain.getNextBillingDate());
        entity.setTrialEndDate(domain.getTrialEndDate());
        entity.setAutoRenew(domain.getAutoRenew());
        entity.setCanceledAt(domain.getCanceledAt());
        return entity;
    }
}
