package com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries.FarmerDashboard;

public interface DashboardQueryService {
    FarmerDashboard getFarmerDashboard(Long farmerUserId);
}
