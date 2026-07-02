package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;

import java.util.List;

public interface FieldVisitQueryService {
    List<FieldVisit> findByAgronomistUserId(Long agronomistUserId);
    List<FieldVisit> findByFarmId(Long farmId);
}
