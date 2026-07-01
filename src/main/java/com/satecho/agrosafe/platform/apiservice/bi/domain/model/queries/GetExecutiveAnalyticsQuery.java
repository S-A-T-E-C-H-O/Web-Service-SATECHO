package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetExecutiveAnalyticsQuery(
        String period,
        String from,
        String to
) {
}
