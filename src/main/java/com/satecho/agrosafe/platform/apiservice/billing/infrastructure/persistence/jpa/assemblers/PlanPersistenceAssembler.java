package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.PlanPersistenceEntity;

import java.util.ArrayList;

public final class PlanPersistenceAssembler {
    private PlanPersistenceAssembler() {}

    public static Plan toDomainFromPersistence(PlanPersistenceEntity e) {
        if (e == null) return null;
        var d = new Plan();
        d.setId(e.getId());
        d.setTier(e.getTier());
        d.setName(e.getName());
        d.setPriceMonthly(e.getPriceMonthly());
        d.setMaxDevices(e.getMaxDevices());
        d.setMaxHectares(e.getMaxHectares());
        d.setMaxFarms(e.getMaxFarms());
        d.setFeatures(e.getFeatures() != null ? new ArrayList<>(e.getFeatures()) : new ArrayList<>());
        return d;
    }

    public static PlanPersistenceEntity toPersistenceFromDomain(Plan d) {
        if (d == null) return null;
        var e = new PlanPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setTier(d.getTier());
        e.setName(d.getName());
        e.setPriceMonthly(d.getPriceMonthly());
        e.setMaxDevices(d.getMaxDevices());
        e.setMaxHectares(d.getMaxHectares());
        e.setMaxFarms(d.getMaxFarms());
        e.setFeatures(d.getFeatures() != null ? new ArrayList<>(d.getFeatures()) : new ArrayList<>());
        return e;
    }
}
