package com.satecho.agrosafe.platform.apiservice.soil.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TelemetryQueryServiceImpl implements TelemetryQueryService {

    private final SensorReadingRepository sensorReadingRepository;

    public TelemetryQueryServiceImpl(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public List<SensorReading> getReadingsByZone(GetReadingsByZoneQuery query) {
        if (query.from() != null && query.to() != null) {
            if (query.metric() != null)
                return sensorReadingRepository.findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(query.zoneId(), query.from(), query.to(), query.metric());
            return sensorReadingRepository.findByZoneIdAndTimestampBetweenOrderByTimestampAsc(query.zoneId(), query.from(), query.to());
        }
        return sensorReadingRepository.findByZoneIdOrderByTimestampDesc(query.zoneId());
    }

    @Override
    public List<SensorReading> getLatestReadingsByZone(GetLatestReadingsByZoneQuery query) {
        return sensorReadingRepository.findLatestByZoneGroupedByMetricType(query.zoneId());
    }

    @Override
    public List<SensorReading> getReadingsByDevice(GetReadingsByDeviceQuery query) {
        return sensorReadingRepository.findByDeviceIdOrderByTimestampDesc(query.deviceId());
    }

    @Override
    public List<SensorReading> getReadingsByTimeRange(GetReadingsByTimeRangeQuery query) {
        if (query.metric() != null)
            return sensorReadingRepository.findByZoneIdAndTimestampBetweenAndMetricTypeOrderByTimestampAsc(query.zoneId(), query.from(), query.to(), query.metric());
        return sensorReadingRepository.findByZoneIdAndTimestampBetweenOrderByTimestampAsc(query.zoneId(), query.from(), query.to());
    }
}
