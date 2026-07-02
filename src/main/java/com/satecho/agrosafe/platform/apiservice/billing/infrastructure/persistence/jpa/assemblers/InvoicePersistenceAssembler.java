package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;

public final class InvoicePersistenceAssembler {
    private InvoicePersistenceAssembler() {}

    public static Invoice toDomainFromPersistence(InvoicePersistenceEntity e) {
        if (e == null) return null;
        var d = new Invoice();
        d.setId(e.getId());
        d.setSubscriptionId(e.getSubscriptionId());
        d.setUserId(e.getUserId());
        d.setAmount(e.getAmount());
        d.setCurrency(e.getCurrency());
        d.setStatus(e.getStatus());
        d.setDescription(e.getDescription());
        d.setIssuedAt(e.getIssuedAt());
        d.setPaidAt(e.getPaidAt());
        return d;
    }

    public static InvoicePersistenceEntity toPersistenceFromDomain(Invoice d) {
        if (d == null) return null;
        var e = new InvoicePersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setSubscriptionId(d.getSubscriptionId());
        e.setUserId(d.getUserId());
        e.setAmount(d.getAmount());
        e.setCurrency(d.getCurrency());
        e.setStatus(d.getStatus());
        e.setDescription(d.getDescription());
        e.setIssuedAt(d.getIssuedAt());
        e.setPaidAt(d.getPaidAt());
        return e;
    }
}
