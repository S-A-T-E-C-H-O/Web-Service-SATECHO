package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.FarmPersistenceEntity;

public final class FarmPersistenceAssembler {

    private FarmPersistenceAssembler() {
    }

    public static Farm toDomainFromPersistence(FarmPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Farm();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setName(entity.getName());
        domain.setLocation(entity.getLocation());
        domain.setHectares(entity.getHectares());
        domain.setCropType(entity.getCropType());
        domain.setActive(entity.getActive() != null ? entity.getActive() : true);
        return domain;
    }

    public static FarmPersistenceEntity toPersistenceFromDomain(Farm farm) {
        if (farm == null) return null;
        var entity = new FarmPersistenceEntity();
        if (farm.getId() != null) {
            entity.setId(farm.getId());
        }
        entity.setUserId(farm.getUserId());
        entity.setName(farm.getName());
        entity.setLocation(farm.getLocation());
        entity.setHectares(farm.getHectares());
        entity.setCropType(farm.getCropType());
        entity.setActive(farm.getActive() != null ? farm.getActive() : true);
        return entity;
    }
}