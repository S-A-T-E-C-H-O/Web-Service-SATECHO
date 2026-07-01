package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;

public final class InvoicePersistenceAssembler {

    private InvoicePersistenceAssembler() {
    }

    public static Invoice toDomainFromPersistence(InvoicePersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Invoice();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setSubscriptionId(entity.getSubscriptionId());
        domain.setAmount(entity.getAmount());
        domain.setCurrency(entity.getCurrency());
        domain.setDescription(entity.getDescription());
        domain.setStatus(entity.getStatus());
        domain.setBillingPeriodStart(entity.getBillingPeriodStart());
        domain.setBillingPeriodEnd(entity.getBillingPeriodEnd());
        domain.setDueDate(entity.getDueDate());
        domain.setPaidAt(entity.getPaidAt());
        domain.setPaymentMethod(entity.getPaymentMethod());
        domain.setTransactionId(entity.getTransactionId());
        return domain;
    }

    public static InvoicePersistenceEntity toPersistenceFromDomain(Invoice domain) {
        if (domain == null) return null;
        var entity = new InvoicePersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUserId(domain.getUserId());
        entity.setSubscriptionId(domain.getSubscriptionId());
        entity.setAmount(domain.getAmount());
        entity.setCurrency(domain.getCurrency());
        entity.setDescription(domain.getDescription());
        entity.setStatus(domain.getStatus());
        entity.setBillingPeriodStart(domain.getBillingPeriodStart());
        entity.setBillingPeriodEnd(domain.getBillingPeriodEnd());
        entity.setDueDate(domain.getDueDate());
        entity.setPaidAt(domain.getPaidAt());
        entity.setPaymentMethod(domain.getPaymentMethod());
        entity.setTransactionId(domain.getTransactionId());
        return entity;
    }
}
