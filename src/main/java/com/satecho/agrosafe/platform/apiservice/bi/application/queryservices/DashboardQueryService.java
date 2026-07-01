package com.satecho.agrosafe.platform.apiservice.bi.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAgronomistDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetFarmerDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetPriorityCasesQuery;

import java.util.Map;

public interface DashboardQueryService {
    Map<String, Object> getFarmerDashboard(GetFarmerDashboardQuery query);
    Map<String, Object> getAgronomistDashboard(GetAgronomistDashboardQuery query);
    Map<String, Object> getPriorityCases(GetPriorityCasesQuery query);
}
