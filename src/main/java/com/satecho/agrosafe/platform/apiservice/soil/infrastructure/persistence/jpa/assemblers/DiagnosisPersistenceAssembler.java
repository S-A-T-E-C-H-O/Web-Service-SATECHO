package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities.DiagnosisPersistenceEntity;

public final class DiagnosisPersistenceAssembler {
    private DiagnosisPersistenceAssembler() {}

    public static Diagnosis toDomainFromPersistence(DiagnosisPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new Diagnosis();
        domain.setId(entity.getId());
        domain.setZoneId(entity.getZoneId());
        domain.setWaterStressIndex(entity.getWaterStressIndex());
        domain.setSoilHealthScore(entity.getSoilHealthScore());
        domain.setRecommendations(entity.getRecommendations());
        domain.setMoistureStatus(entity.getMoistureStatus());
        domain.setEcStatus(entity.getEcStatus());
        domain.setPhStatus(entity.getPhStatus());
        domain.setTemperatureStatus(entity.getTemperatureStatus());
        domain.setGeneratedAt(entity.getGeneratedAt());
        return domain;
    }

    public static DiagnosisPersistenceEntity toPersistenceFromDomain(Diagnosis domain) {
        if (domain == null) return null;
        var entity = new DiagnosisPersistenceEntity();
        if (domain.getId() != null) entity.setId(domain.getId());
        entity.setZoneId(domain.getZoneId());
        entity.setWaterStressIndex(domain.getWaterStressIndex());
        entity.setSoilHealthScore(domain.getSoilHealthScore());
        entity.setRecommendations(domain.getRecommendations());
        entity.setMoistureStatus(domain.getMoistureStatus());
        entity.setEcStatus(domain.getEcStatus());
        entity.setPhStatus(domain.getPhStatus());
        entity.setTemperatureStatus(domain.getTemperatureStatus());
        entity.setGeneratedAt(domain.getGeneratedAt());
        return entity;
    }
}
