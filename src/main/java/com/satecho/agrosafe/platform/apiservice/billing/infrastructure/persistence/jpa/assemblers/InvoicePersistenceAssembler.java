package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;

/**
 * Mapper utility to convert invoices between the core domain model and JPA database entities.
 */
public final class InvoicePersistenceAssembler {
    
    /**
     * Private constructor to prevent instantiation of utility mapper class.
     */
    private InvoicePersistenceAssembler() {}

    /**
     * Converts a JPA persistence entity into a domain Invoice aggregate root.
     *
     * @param e the database entity
     * @return the initialized domain aggregate, or null if the entity is null
     */
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

    /**
     * Converts a domain Invoice aggregate root into a JPA persistence entity.
     *
     * @param d the domain aggregate
     * @return the database persistence entity, or null if the domain object is null
     */
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
