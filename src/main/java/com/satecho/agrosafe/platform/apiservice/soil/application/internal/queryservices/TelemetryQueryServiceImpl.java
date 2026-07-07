package com.satecho.agrosafe.platform.apiservice.soil.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.edge.application.services.DemoSharedDeviceLinkService;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TelemetryQueryServiceImpl implements TelemetryQueryService {

    private final SensorReadingRepository sensorReadingRepository;
    private final DemoSharedDeviceLinkService demoSharedDeviceLinkService;

    public TelemetryQueryServiceImpl(SensorReadingRepository sensorReadingRepository,
                                     DemoSharedDeviceLinkService demoSharedDeviceLinkService) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.demoSharedDeviceLinkService = demoSharedDeviceLinkService;
    }

    @Override
    public List<SensorReading> getReadingsByZone(GetReadingsByZoneQuery query) {
        var demoReadings = getDemoReadingsByZone(query.zoneId(), query.from(), query.to(), query.metric());
        if (!demoReadings.isEmpty()) return demoReadings;
        if (query.from() != null && query.to() != null) {
            if (query.metric() != null)
                return sensorReadingRepository.findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(query.zoneId(), query.from(), query.to(), query.metric());
            return sensorReadingRepository.findByZoneIdAndTimestampBetweenOrderByTimestampAsc(query.zoneId(), query.from(), query.to());
        }
        return sensorReadingRepository.findByZoneIdOrderByTimestampDesc(query.zoneId());
    }

    @Override
    public List<SensorReading> getLatestReadingsByZone(GetLatestReadingsByZoneQuery query) {
        var demoLatest = getLatestDemoReadingsByZone(query.zoneId());
        if (!demoLatest.isEmpty()) return demoLatest;
        return sensorReadingRepository.findLatestByZoneGroupedByMetricType(query.zoneId());
    }

    @Override
    public List<SensorReading> getReadingsByDevice(GetReadingsByDeviceQuery query) {
        return sensorReadingRepository.findByDeviceIdOrderByTimestampDesc(query.deviceId());
    }

    @Override
    public List<SensorReading> getReadingsByTimeRange(GetReadingsByTimeRangeQuery query) {
        var demoReadings = getDemoReadingsByZone(query.zoneId(), query.from(), query.to(), query.metric());
        if (!demoReadings.isEmpty()) return demoReadings;
        if (query.metric() != null)
            return sensorReadingRepository.findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(query.zoneId(), query.from(), query.to(), query.metric());
        return sensorReadingRepository.findByZoneIdAndTimestampBetweenOrderByTimestampAsc(query.zoneId(), query.from(), query.to());
    }

    @Override
    public long countReadingsSince(Instant since) {
        return sensorReadingRepository.countByTimestampAfter(since);
    }

    private List<SensorReading> getLatestDemoReadingsByZone(Long zoneId) {
        return demoSharedDeviceLinkService.findActiveByZoneId(zoneId)
                .map(link -> sensorReadingRepository.findLatestByDeviceGroupedByMetricType(link.getPhysicalDeviceId()).stream()
                        .map(reading -> demoSharedDeviceLinkService.mapReadingToLinkedZone(reading, link.getZoneId()))
                        .toList())
                .orElse(List.of());
    }

    private List<SensorReading> getDemoReadingsByZone(Long zoneId, Instant from, Instant to,
                                                      com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType metric) {
        if (from == null || to == null) return List.of();
        return demoSharedDeviceLinkService.findActiveByZoneId(zoneId)
                .map(link -> {
                    var readings = metric != null
                            ? sensorReadingRepository.findByDeviceIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(
                                    link.getPhysicalDeviceId(), from, to, metric)
                            : sensorReadingRepository.findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(
                                    link.getPhysicalDeviceId(), from, to);
                    return readings.stream()
                            .map(reading -> demoSharedDeviceLinkService.mapReadingToLinkedZone(reading, link.getZoneId()))
                            .toList();
                })
                .orElse(List.of());
    }
}
