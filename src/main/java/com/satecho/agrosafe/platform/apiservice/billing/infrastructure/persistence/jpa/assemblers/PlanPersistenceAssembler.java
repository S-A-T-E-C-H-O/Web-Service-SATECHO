package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.PlanPersistenceEntity;

import java.util.ArrayList;

/**
 * Mapper utility to convert subscription plans between domain models and JPA database entities.
 */
public final class PlanPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of utility mapper class.
     */
    private PlanPersistenceAssembler() {}

    /**
     * Converts a JPA persistence entity into a domain Plan aggregate root.
     *
     * @param e the database entity
     * @return the initialized domain aggregate, or null if the entity is null
     */
    public static Plan toDomainFromPersistence(PlanPersistenceEntity e) {
        if (e == null) return null;
        var d = new Plan();
        d.setId(e.getId());
        d.setTier(e.getTier());
        d.setName(e.getName());
        d.setPriceMonthly(e.getPriceMonthly());
        d.setMaxDevices(e.getMaxDevices());
        d.setMaxFarms(e.getMaxFarms());
        d.setFeatures(e.getFeatures() != null ? new ArrayList<>(e.getFeatures()) : new ArrayList<>());
        return d;
    }

    /**
     * Converts a domain Plan aggregate root into a JPA persistence entity.
     *
     * @param d the domain aggregate
     * @return the database persistence entity, or null if the domain object is null
     */
    public static PlanPersistenceEntity toPersistenceFromDomain(Plan d) {
        if (d == null) return null;
        var e = new PlanPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setTier(d.getTier());
        e.setName(d.getName());
        e.setPriceMonthly(d.getPriceMonthly());
        e.setMaxDevices(d.getMaxDevices());
        e.setMaxFarms(d.getMaxFarms());
        e.setFeatures(d.getFeatures() != null ? new ArrayList<>(d.getFeatures()) : new ArrayList<>());
        return e;
    }
}
