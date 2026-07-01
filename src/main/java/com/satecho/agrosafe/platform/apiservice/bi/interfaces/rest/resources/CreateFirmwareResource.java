package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

public record CreateFirmwareResource(
        String version,
        String deviceModel,
        String changelog,
        String binaryUrl
) {
}
