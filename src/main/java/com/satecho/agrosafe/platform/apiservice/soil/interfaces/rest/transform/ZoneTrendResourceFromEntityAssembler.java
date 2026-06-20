package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.ZoneTrendResource;
import java.util.List;

public class ZoneTrendResourceFromEntityAssembler {
    private ZoneTrendResourceFromEntityAssembler() {}
    public static ZoneTrendResource toResourceFromReadings(MetricType metricType, List<SensorReading> readings) {
        List<ZoneTrendResource.DataPoint> dataPoints = readings.stream()
                .map(r -> new ZoneTrendResource.DataPoint(r.getTimestamp(), r.getValue())).toList();
        return new ZoneTrendResource(readings.isEmpty() ? null : readings.get(0).getZoneId(),
                metricType.name(), metricType.getUnit(), dataPoints, metricType.getMinValid(), metricType.getMaxValid());
    }
}
