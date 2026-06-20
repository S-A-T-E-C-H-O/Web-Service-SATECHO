package com.satecho.agrosafe.platform.apiservice.security.domain.model.queries;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import java.time.Instant;

public record GetSecurityEventsByFarmQuery(Long farmId, Instant from, Instant to, EventSeverity severity,
                                           EventClassification classification, Integer limit, Integer page) {
    public GetSecurityEventsByFarmQuery {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        if (limit == null || limit < 1) limit = 20;
        if (limit > 100) limit = 100;
        if (page == null || page < 0) page = 0;
    }
}
