package com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import java.time.Instant;

public record GetReadingsByTimeRangeQuery(Long zoneId, Instant from, Instant to, MetricType metric) {
    public GetReadingsByTimeRangeQuery { if (zoneId == null) throw new IllegalArgumentException("Zone ID is required"); }
}
