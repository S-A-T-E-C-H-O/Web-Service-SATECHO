package com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;

import java.util.List;
import java.util.Optional;

public interface FieldVisitRepository {
    FieldVisit save(FieldVisit visit);
    Optional<FieldVisit> findById(Long id);
    List<FieldVisit> findByAgronomistId(Long agronomistId);
}
