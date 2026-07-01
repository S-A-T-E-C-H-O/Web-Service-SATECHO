package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

import java.util.List;

public record BatchRegisterDevicesCommand(
        Long userId,
        List<BatchDeviceEntry> devices
) {
    public BatchRegisterDevicesCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (devices == null || devices.isEmpty()) {
            throw new IllegalArgumentException("At least one device entry is required");
        }
    }

    public record BatchDeviceEntry(
            String serialNumber,
            String type
    ) {
        public BatchDeviceEntry {
            if (serialNumber == null || serialNumber.isBlank()) {
                throw new IllegalArgumentException("Serial number is required");
            }
            if (type == null || type.isBlank()) {
                throw new IllegalArgumentException("Device type is required");
            }
        }
    }
}
