package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.assemblers.SensorReadingPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.repositories.SensorReadingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public class SensorReadingRepositoryImpl implements SensorReadingRepository {

    private final SensorReadingPersistenceRepository persistenceRepository;

    public SensorReadingRepositoryImpl(SensorReadingPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override public SensorReading save(SensorReading reading) {
        var saved = persistenceRepository.save(SensorReadingPersistenceAssembler.toPersistenceFromDomain(reading));
        return SensorReadingPersistenceAssembler.toDomainFromPersistence(saved);
    }
    @Override public List<SensorReading> findByZoneIdOrderByTimestampDesc(Long zoneId) {
        return persistenceRepository.findByZoneIdOrderByTimestampDesc(zoneId).stream().map(SensorReadingPersistenceAssembler::toDomainFromPersistence).toList();
    }
    @Override public List<SensorReading> findByZoneIdAndTimestampBetweenOrderByTimestampAsc(Long zoneId, Instant from, Instant to) {
        return persistenceRepository.findByZoneIdAndTimestampBetweenOrderByTimestampAsc(zoneId, from, to).stream().map(SensorReadingPersistenceAssembler::toDomainFromPersistence).toList();
    }
    @Override public List<SensorReading> findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(Long zoneId, Instant from, Instant to, MetricType metricType) {
        return persistenceRepository.findByZoneIdAndTimestampBetweenAndReadingValue_MetricTypeOrderByTimestampAsc(zoneId, from, to, metricType).stream().map(SensorReadingPersistenceAssembler::toDomainFromPersistence).toList();
    }
    @Override public List<SensorReading> findByDeviceIdOrderByTimestampDesc(Long deviceId) {
        return persistenceRepository.findByDeviceIdOrderByTimestampDesc(deviceId).stream().map(SensorReadingPersistenceAssembler::toDomainFromPersistence).toList();
    }
    @Override public List<SensorReading> findLatestByZoneGroupedByMetricType(Long zoneId) {
        return persistenceRepository.findLatestByZoneGroupedByMetricType(zoneId).stream().map(SensorReadingPersistenceAssembler::toDomainFromPersistence).toList();
    }
    @Override public Optional<SensorReading> findLatestByDeviceId(Long deviceId) {
        return persistenceRepository.findLatestByDeviceId(deviceId).map(SensorReadingPersistenceAssembler::toDomainFromPersistence);
    }
    @Override public long countByTimestampAfter(Instant since) {
        return persistenceRepository.countByTimestampAfter(since);
    }
}
