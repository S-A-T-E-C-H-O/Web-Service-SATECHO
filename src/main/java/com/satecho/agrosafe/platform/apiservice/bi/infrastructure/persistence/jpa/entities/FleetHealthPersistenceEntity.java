package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "fleet_health")
@Getter
@Setter
@NoArgsConstructor
public class FleetHealthPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "total_devices", nullable = false)
    private Integer totalDevices;

    @Column(name = "online_devices", nullable = false)
    private Integer onlineDevices;

    @Column(name = "offline_devices", nullable = false)
    private Integer offlineDevices;

    @Column(name = "error_devices", nullable = false)
    private Integer errorDevices;

    @Column(name = "low_battery_devices", nullable = false)
    private Integer lowBatteryDevices;

    @Column(name = "devices_by_type", columnDefinition = "TEXT")
    private String devicesByType;

    @Column(name = "average_signal_strength")
    private Double averageSignalStrength;

    @Column(name = "snapshot_at", nullable = false)
    private Instant snapshotAt;
}
