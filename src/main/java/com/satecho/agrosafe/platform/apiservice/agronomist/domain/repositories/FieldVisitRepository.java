package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;

import java.util.List;
import java.util.Optional;

public interface FieldVisitRepository {
    FieldVisit save(FieldVisit visit);
    Optional<FieldVisit> findById(Long id);
    List<FieldVisit> findByAgronomistUserIdOrderByScheduledAtAsc(Long agronomistUserId);
    List<FieldVisit> findByFarmIdOrderByScheduledAtDesc(Long farmId);
}
