package com.satecho.agrosafe.platform.apiservice.soil.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.*;

import java.time.Instant;
import java.util.List;

public interface TelemetryQueryService {
    List<SensorReading> getReadingsByZone(GetReadingsByZoneQuery query);
    List<SensorReading> getLatestReadingsByZone(GetLatestReadingsByZoneQuery query);
    List<SensorReading> getReadingsByDevice(GetReadingsByDeviceQuery query);
    List<SensorReading> getReadingsByTimeRange(GetReadingsByTimeRangeQuery query);
    long countReadingsSince(Instant since);
}
