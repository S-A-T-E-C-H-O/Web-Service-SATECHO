package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;

public final class PaymentPersistenceAssembler {

    private PaymentPersistenceAssembler() {
    }

    public static Payment toDomainFromPersistence(PaymentPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Payment();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setSubscriptionId(entity.getSubscriptionId());
        domain.setInvoiceId(entity.getInvoiceId());
        domain.setAmount(entity.getAmount());
        domain.setCurrency(entity.getCurrency());
        domain.setPaymentMethod(entity.getPaymentMethod());
        domain.setStatus(entity.getStatus());
        domain.setTransactionId(entity.getTransactionId());
        domain.setProcessedAt(entity.getProcessedAt());
        return domain;
    }

    public static PaymentPersistenceEntity toPersistenceFromDomain(Payment domain) {
        if (domain == null) return null;
        var entity = new PaymentPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUserId(domain.getUserId());
        entity.setSubscriptionId(domain.getSubscriptionId());
        entity.setInvoiceId(domain.getInvoiceId());
        entity.setAmount(domain.getAmount());
        entity.setCurrency(domain.getCurrency());
        entity.setPaymentMethod(domain.getPaymentMethod());
        entity.setStatus(domain.getStatus());
        entity.setTransactionId(domain.getTransactionId());
        entity.setProcessedAt(domain.getProcessedAt());
        return entity;
    }
}
