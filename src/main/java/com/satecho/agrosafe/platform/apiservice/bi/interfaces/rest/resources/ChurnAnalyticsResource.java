package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record ChurnAnalyticsResource(
        Double churnRate,
        String period,
        Integer canceledSubscriptions,
        Integer activeSubscriptions,
        String segment,
        String rawData
) {
}
