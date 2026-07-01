package com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class FirmwareRelease extends AuditableAbstractAggregateRoot<FirmwareRelease> {

    @Setter private Long id;
    @Setter private String version;
    @Setter private String deviceModel;
    @Setter private String changelog;
    @Setter private String binaryUrl;
    @Setter private Instant releasedAt;
    @Setter private boolean active;

    public FirmwareRelease() {
    }

    public FirmwareRelease(String version, String deviceModel, String changelog,
                           String binaryUrl) {
        this.version = version;
        this.deviceModel = deviceModel;
        this.changelog = changelog;
        this.binaryUrl = binaryUrl;
        this.releasedAt = Instant.now();
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
