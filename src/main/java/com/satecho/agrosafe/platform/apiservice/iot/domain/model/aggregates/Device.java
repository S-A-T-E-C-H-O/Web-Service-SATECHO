package com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceCredentials;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Device extends AuditableAbstractAggregateRoot<Device> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private String serialNumber;
    @Setter private DeviceType type;
    @Setter private DeviceStatus status;
    @Setter private DeviceCredentials credentials;
    @Setter private String firmwareVersion;
    @Setter private Double batteryLevel;
    @Setter private Instant lastHeartbeatAt;
    @Setter private Instant lastTelemetryAt;
    @Setter private String healthStatus;

    public Device() {
    }

    public Device(Long userId, String serialNumber, DeviceType type) {
        this.userId = userId;
        this.serialNumber = serialNumber;
        this.type = type;
        this.status = DeviceStatus.PENDING_ACTIVATION;
        this.healthStatus = "HEALTHY";
    }

    public void activate(String certificateThumbprint) {
        if (this.status != DeviceStatus.PENDING_ACTIVATION)
            throw new IllegalStateException("Cannot activate device in status: " + this.status);
        String apiKey = UUID.randomUUID().toString().replace("-", "");
        this.credentials = DeviceCredentials.of(apiKey, certificateThumbprint);
        this.status = DeviceStatus.ACTIVE;
    }

    public void deactivate() {
        if (this.status != DeviceStatus.ACTIVE)
            throw new IllegalStateException("Cannot deactivate device in status: " + this.status);
        this.status = DeviceStatus.INACTIVE;
    }

    public void decommission() {
        if (this.status == DeviceStatus.DECOMMISSIONED)
            throw new IllegalStateException("Device is already decommissioned");
        this.status = DeviceStatus.DECOMMISSIONED;
    }

    public void updateFirmware(String newFirmwareVersion) {
        if (newFirmwareVersion == null || newFirmwareVersion.isBlank())
            throw new IllegalArgumentException("Firmware version must not be null or blank");
        this.firmwareVersion = newFirmwareVersion;
    }

    public void recordHeartbeat() {
        this.lastHeartbeatAt = Instant.now();
    }

    public void recordTelemetry() {
        this.lastTelemetryAt = Instant.now();
    }

    public void updateBatteryLevel(Double newBatteryLevel) {
        if (newBatteryLevel == null || newBatteryLevel < 0.0 || newBatteryLevel > 100.0)
            throw new IllegalArgumentException("Battery level must be between 0 and 100");
        this.batteryLevel = newBatteryLevel;
    }

    public void degradeHealth(String newHealthStatus) {
        this.healthStatus = newHealthStatus;
    }

    public void restoreHealth() {
        this.healthStatus = "HEALTHY";
    }

    public boolean isWithinHeartbeatWindow() {
        if (this.lastHeartbeatAt == null) return false;
        return Duration.between(this.lastHeartbeatAt, Instant.now()).toMinutes() <= 5;
    }

    public boolean getOnline() { return isWithinHeartbeatWindow(); }
}
