package com.satecho.agrosafe.platform.apiservice.soil.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetDiagnosisQuery;

import java.util.Optional;

public interface DiagnosisQueryService {
    Optional<Diagnosis> findById(GetDiagnosisQuery query);
    Optional<Diagnosis> findLatestByZoneId(Long zoneId);
}
