package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

public record CreateFirmwareCommand(
        String version,
        String deviceModel,
        String changelog,
        String binaryUrl
) {
    public CreateFirmwareCommand {
        if (version == null || version.isBlank()) {
            throw new IllegalArgumentException("Version is required");
        }
        if (deviceModel == null || deviceModel.isBlank()) {
            throw new IllegalArgumentException("Device model is required");
        }
        if (binaryUrl == null || binaryUrl.isBlank()) {
            throw new IllegalArgumentException("Binary URL is required");
        }
    }
}
