package com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class FleetHealth extends AuditableAbstractAggregateRoot<FleetHealth> {

    @Setter private Long id;
    @Setter private Integer totalDevices;
    @Setter private Integer onlineDevices;
    @Setter private Integer offlineDevices;
    @Setter private Integer errorDevices;
    @Setter private Integer lowBatteryDevices;
    @Setter private String devicesByType;
    @Setter private Double averageSignalStrength;
    @Setter private Instant snapshotAt;

    public FleetHealth() {
    }

    public FleetHealth(Integer totalDevices, Integer onlineDevices, Integer offlineDevices,
                       Integer errorDevices, Integer lowBatteryDevices, String devicesByType,
                       Double averageSignalStrength) {
        this.totalDevices = totalDevices;
        this.onlineDevices = onlineDevices;
        this.offlineDevices = offlineDevices;
        this.errorDevices = errorDevices;
        this.lowBatteryDevices = lowBatteryDevices;
        this.devicesByType = devicesByType;
        this.averageSignalStrength = averageSignalStrength;
        this.snapshotAt = Instant.now();
    }
}
