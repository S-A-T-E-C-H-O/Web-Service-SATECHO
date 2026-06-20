package com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries;

public record GetReadingsByDeviceQuery(Long deviceId) {
    public GetReadingsByDeviceQuery { if (deviceId == null) throw new IllegalArgumentException("Device ID is required"); }
}
