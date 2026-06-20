package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.ReadingValue;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities.ReadingValueEmbeddable;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities.SensorReadingPersistenceEntity;

public final class SensorReadingPersistenceAssembler {
    private SensorReadingPersistenceAssembler() {}

    public static SensorReading toDomainFromPersistence(SensorReadingPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new SensorReading();
        domain.setId(entity.getId());
        domain.setDeviceId(entity.getDeviceId());
        domain.setZoneId(entity.getZoneId());
        if (entity.getReadingValue() != null) {
            domain.setReadingValue(ReadingValue.of(entity.getReadingValue().getMetricType(), entity.getReadingValue().getValue()));
        }
        domain.setTimestamp(entity.getTimestamp());
        domain.setIngestedAt(entity.getIngestedAt());
        domain.setIsValid(entity.getIsValid());
        return domain;
    }

    public static SensorReadingPersistenceEntity toPersistenceFromDomain(SensorReading domain) {
        if (domain == null) return null;
        var entity = new SensorReadingPersistenceEntity();
        if (domain.getId() != null) entity.setId(domain.getId());
        entity.setDeviceId(domain.getDeviceId());
        entity.setZoneId(domain.getZoneId());
        if (domain.getReadingValue() != null) {
            var rv = new ReadingValueEmbeddable();
            rv.setMetricType(domain.getReadingValue().metricType());
            rv.setValue(domain.getReadingValue().value());
            rv.setUnit(domain.getReadingValue().unit());
            entity.setReadingValue(rv);
        }
        entity.setTimestamp(domain.getTimestamp());
        entity.setIngestedAt(domain.getIngestedAt());
        entity.setIsValid(domain.getIsValid());
        return entity;
    }
}
