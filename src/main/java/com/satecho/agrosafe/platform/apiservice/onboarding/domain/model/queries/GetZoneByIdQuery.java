package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.queries;

public record GetZoneByIdQuery(Long zoneId) {
    public GetZoneByIdQuery {
        if (zoneId == null) throw new IllegalArgumentException("Zone ID is required");
    }
}