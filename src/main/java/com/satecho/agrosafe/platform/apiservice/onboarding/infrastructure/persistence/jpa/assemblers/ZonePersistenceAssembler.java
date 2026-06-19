package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.IrrigationZonePersistenceEntity;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.ThresholdLimitsEmbeddable;

public final class ZonePersistenceAssembler {

    private ZonePersistenceAssembler() {
    }

    public static IrrigationZone toDomainFromPersistence(IrrigationZonePersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new IrrigationZone();
        domain.setId(entity.getId());
        domain.setFarmId(entity.getFarmId());
        domain.setName(entity.getName());
        domain.setAreaHectares(entity.getAreaHectares());
        domain.setCropType(entity.getCropType());
        domain.setDeviceId(entity.getDeviceId());
        if (entity.getThresholds() != null) {
            domain.setThresholds(new ThresholdLimits(
                    entity.getThresholds().getMinMoisture(),
                    entity.getThresholds().getMaxMoisture(),
                    entity.getThresholds().getMinEc(),
                    entity.getThresholds().getMaxEc(),
                    entity.getThresholds().getMinPh(),
                    entity.getThresholds().getMaxPh(),
                    entity.getThresholds().getMinTemperature(),
                    entity.getThresholds().getMaxTemperature()));
        }
        return domain;
    }

    public static IrrigationZonePersistenceEntity toPersistenceFromDomain(IrrigationZone zone) {
        if (zone == null) return null;
        var entity = new IrrigationZonePersistenceEntity();
        if (zone.getId() != null) {
            entity.setId(zone.getId());
        }
        entity.setFarmId(zone.getFarmId());
        entity.setName(zone.getName());
        entity.setAreaHectares(zone.getAreaHectares());
        entity.setCropType(zone.getCropType());
        entity.setDeviceId(zone.getDeviceId());
        if (zone.getThresholds() != null) {
            var thresholds = new ThresholdLimitsEmbeddable();
            thresholds.setMinMoisture(zone.getThresholds().minMoisture());
            thresholds.setMaxMoisture(zone.getThresholds().maxMoisture());
            thresholds.setMinEc(zone.getThresholds().minEc());
            thresholds.setMaxEc(zone.getThresholds().maxEc());
            thresholds.setMinPh(zone.getThresholds().minPh());
            thresholds.setMaxPh(zone.getThresholds().maxPh());
            thresholds.setMinTemperature(zone.getThresholds().minTemperature());
            thresholds.setMaxTemperature(zone.getThresholds().maxTemperature());
            entity.setThresholds(thresholds);
        }
        return entity;
    }
}