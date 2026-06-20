package com.satecho.agrosafe.platform.apiservice.soil.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;

import java.util.Optional;

public interface DiagnosisRepository {
    Diagnosis save(Diagnosis diagnosis);
    Optional<Diagnosis> findById(Long id);
    Optional<Diagnosis> findTopByZoneIdOrderByGeneratedAtDesc(Long zoneId);
}
