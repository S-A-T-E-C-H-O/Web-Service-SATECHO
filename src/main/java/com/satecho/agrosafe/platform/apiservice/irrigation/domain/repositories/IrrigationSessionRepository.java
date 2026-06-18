package com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.IrrigationStatus;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IrrigationSessionRepository {
    IrrigationSession save(IrrigationSession session);
    Optional<IrrigationSession> findByZoneIdAndStatus(Long zoneId, IrrigationStatus status);
    Optional<IrrigationSession> findActiveByZoneId(Long zoneId);
    List<IrrigationSession> findByZoneIdOrderByStartedAtDesc(Long zoneId, int limit);
    List<IrrigationSession> findByZoneIdAndStartedAtBetween(Long zoneId, Instant from, Instant to, int limit);
}
