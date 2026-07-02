package com.satecho.agrosafe.platform.apiservice.soil.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SensorReadingRepository {
    SensorReading save(SensorReading reading);
    List<SensorReading> findByZoneIdOrderByTimestampDesc(Long zoneId);
    List<SensorReading> findByZoneIdAndTimestampBetweenOrderByTimestampAsc(Long zoneId, Instant from, Instant to);
    List<SensorReading> findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(Long zoneId, Instant from, Instant to, MetricType metricType);
    List<SensorReading> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    List<SensorReading> findLatestByZoneGroupedByMetricType(Long zoneId);
    Optional<SensorReading> findLatestByDeviceId(Long deviceId);
    long countByTimestampAfter(Instant since);
}
