package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "firmware_releases")
@Getter
@Setter
@NoArgsConstructor
public class FirmwareReleasePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false, length = 50)
    private String version;

    @Column(name = "device_model", nullable = false, length = 100)
    private String deviceModel;

    @Column(columnDefinition = "TEXT")
    private String changelog;

    @Column(name = "binary_url", nullable = false, length = 500)
    private String binaryUrl;

    @Column(name = "released_at", nullable = false)
    private Instant releasedAt;

    @Column(nullable = false)
    private boolean active;
}
