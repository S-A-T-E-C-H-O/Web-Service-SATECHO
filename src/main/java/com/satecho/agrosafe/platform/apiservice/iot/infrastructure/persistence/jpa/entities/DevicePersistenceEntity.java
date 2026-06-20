package com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
public class DevicePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "serial_number", nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private DeviceType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private DeviceStatus status;

    @Embedded
    private DeviceCredentialsEmbeddable credentials;

    @Column(name = "firmware_version", length = 30)
    private String firmwareVersion;

    @Column(name = "battery_level")
    private Double batteryLevel;

    @Column(name = "last_heartbeat_at")
    private Instant lastHeartbeatAt;

    @Column(name = "last_telemetry_at")
    private Instant lastTelemetryAt;

    @Column(name = "health_status", nullable = false, length = 20)
    private String healthStatus;
}
