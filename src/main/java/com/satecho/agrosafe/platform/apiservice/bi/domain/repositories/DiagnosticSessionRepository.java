package com.satecho.agrosafe.platform.apiservice.bi.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;

import java.util.List;
import java.util.Optional;

public interface DiagnosticSessionRepository {
    Optional<DiagnosticSession> findById(Long id);
    List<DiagnosticSession> findByDeviceIdOrderByStartedAtDesc(Long deviceId);
    DiagnosticSession save(DiagnosticSession session);
}
