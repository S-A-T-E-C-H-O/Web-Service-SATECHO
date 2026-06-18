package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries;

import java.time.Instant;

public record GetSessionHistoryByZoneQuery(Long zoneId, Instant fromDate, Instant toDate, Integer limit) {
    public GetSessionHistoryByZoneQuery { if (limit == null || limit < 1 || limit > 100) limit = 20; }
}
