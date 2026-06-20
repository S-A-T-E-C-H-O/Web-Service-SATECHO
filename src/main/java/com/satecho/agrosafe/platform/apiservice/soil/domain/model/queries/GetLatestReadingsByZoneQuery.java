package com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries;

public record GetLatestReadingsByZoneQuery(Long zoneId) {
    public GetLatestReadingsByZoneQuery { if (zoneId == null) throw new IllegalArgumentException("Zone ID is required"); }
}
