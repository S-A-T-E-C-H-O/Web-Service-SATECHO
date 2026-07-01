package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources;

import java.time.Instant;

public record FirmwareResource(
        Long id,
        String version,
        String deviceModel,
        String changelog,
        String binaryUrl,
        Instant releasedAt,
        boolean active,
        Instant createdAt
) {
}
