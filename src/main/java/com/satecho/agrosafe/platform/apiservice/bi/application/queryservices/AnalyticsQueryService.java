package com.satecho.agrosafe.platform.apiservice.bi.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetChurnAnalysisQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetExecutiveAnalyticsQuery;

public interface AnalyticsQueryService {
    AnalyticsSnapshot getExecutiveAnalytics(GetExecutiveAnalyticsQuery query);
    AnalyticsSnapshot getChurnAnalysis(GetChurnAnalysisQuery query);
}
