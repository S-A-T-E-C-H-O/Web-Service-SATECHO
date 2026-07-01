package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetAgronomistDashboardQuery(
        Long userId
) {
    public GetAgronomistDashboardQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}
