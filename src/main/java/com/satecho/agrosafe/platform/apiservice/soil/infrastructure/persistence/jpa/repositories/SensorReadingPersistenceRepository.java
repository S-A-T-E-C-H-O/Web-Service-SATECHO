package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities.SensorReadingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorReadingPersistenceRepository extends JpaRepository<SensorReadingPersistenceEntity, Long> {
    List<SensorReadingPersistenceEntity> findByZoneIdOrderByTimestampDesc(Long zoneId);
    List<SensorReadingPersistenceEntity> findByZoneIdAndTimestampBetweenOrderByTimestampAsc(Long zoneId, Instant from, Instant to);
    List<SensorReadingPersistenceEntity> findByZoneIdAndTimestampBetweenAndReadingValue_MetricTypeOrderByTimestampAsc(Long zoneId, Instant from, Instant to, MetricType metricType);
    List<SensorReadingPersistenceEntity> findByDeviceIdOrderByTimestampDesc(Long deviceId);

    @Query("SELECT r FROM SensorReadingPersistenceEntity r WHERE r.zoneId = :zoneId AND r.timestamp IN (SELECT MAX(r2.timestamp) FROM SensorReadingPersistenceEntity r2 WHERE r2.zoneId = :zoneId GROUP BY r2.readingValue.metricType) ORDER BY r.readingValue.metricType")
    List<SensorReadingPersistenceEntity> findLatestByZoneGroupedByMetricType(@Param("zoneId") Long zoneId);

    @Query("SELECT r FROM SensorReadingPersistenceEntity r WHERE r.deviceId = :deviceId AND r.timestamp = (SELECT MAX(r2.timestamp) FROM SensorReadingPersistenceEntity r2 WHERE r2.deviceId = :deviceId)")
    Optional<SensorReadingPersistenceEntity> findLatestByDeviceId(@Param("deviceId") Long deviceId);

    long countByTimestampAfter(Instant since);
}
