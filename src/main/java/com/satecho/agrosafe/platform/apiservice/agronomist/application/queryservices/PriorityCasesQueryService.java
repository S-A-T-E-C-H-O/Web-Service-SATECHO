package com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.PriorityCase;

import java.util.List;

public interface PriorityCasesQueryService {
    List<PriorityCase> findPriorityCases(Long agronomistUserId);
}
