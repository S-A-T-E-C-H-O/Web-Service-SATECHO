package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

import java.time.Instant;

public record ExecutiveAnalyticsResource(
        Long id,
        String period,
        Instant fromDate,
        Instant toDate,
        Integer activeUsers,
        Double mrr,
        String currency,
        Double conversionRate,
        Double churnRate,
        Integer newSubscriptions,
        Integer canceledSubscriptions,
        String rawData
) {
}
