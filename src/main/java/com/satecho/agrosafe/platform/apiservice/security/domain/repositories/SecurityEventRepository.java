package com.satecho.agrosafe.platform.apiservice.security.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface SecurityEventRepository {
    SecurityEvent save(SecurityEvent event);
    Optional<SecurityEvent> findById(Long id);
    List<SecurityEvent> findByFarmIdWithFilters(Long farmId, Instant from, Instant to,
                                                EventSeverity severity, EventClassification classification,
                                                int limit, int page);
    List<SecurityEvent> findByDeviceIdWithFilters(Long deviceId, Instant from, Instant to,
                                                  EventSeverity severity, EventClassification classification,
                                                  int limit, int page);
}
