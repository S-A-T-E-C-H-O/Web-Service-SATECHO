package com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.ActuatorLog;

import java.time.Instant;
import java.util.List;

public interface ActuatorLogRepository {
    ActuatorLog save(ActuatorLog log);
    List<ActuatorLog> findByDeviceIdOrderByExecutedAtDesc(Long deviceId, int limit);
    List<ActuatorLog> findByDeviceIdAndExecutedAtBetween(Long deviceId, Instant from, Instant to, int limit);
}
