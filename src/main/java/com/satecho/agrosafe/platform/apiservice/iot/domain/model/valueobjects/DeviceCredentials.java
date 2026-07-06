package com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects;

public record DeviceCredentials(String apiKey, String certificateThumbprint) {
    public DeviceCredentials {
        if (apiKey == null || apiKey.isBlank()) throw new IllegalArgumentException("API key must not be null or blank");
    }
    public static DeviceCredentials of(String apiKey, String certificateThumbprint) {
        return new DeviceCredentials(apiKey, certificateThumbprint);
    }
}
