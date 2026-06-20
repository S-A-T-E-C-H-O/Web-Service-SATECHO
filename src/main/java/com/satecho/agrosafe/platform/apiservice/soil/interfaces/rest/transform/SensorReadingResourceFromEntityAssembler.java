package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.SensorReadingResource;

public class SensorReadingResourceFromEntityAssembler {
    private SensorReadingResourceFromEntityAssembler() {}
    public static SensorReadingResource toResourceFromEntity(SensorReading entity) {
        return new SensorReadingResource(entity.getId(), entity.getDeviceId(), entity.getZoneId(),
                entity.getMetricType() != null ? entity.getMetricType().name() : null,
                entity.getValue(), entity.getUnit(), entity.getTimestamp(), entity.getIngestedAt(), entity.getIsValid());
    }
}
