package com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries.GetVisitsByAgronomistQuery;

import java.util.List;

public interface FieldVisitQueryService {
    List<FieldVisit> handle(GetVisitsByAgronomistQuery query);
}
