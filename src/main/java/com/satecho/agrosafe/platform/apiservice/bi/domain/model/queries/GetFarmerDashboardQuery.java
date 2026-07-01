package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetFarmerDashboardQuery(
        Long userId
) {
    public GetFarmerDashboardQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}
