package com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands;

public record StartDiagnosticCommand(
        Long deviceId
) {
    public StartDiagnosticCommand {
        if (deviceId == null) {
            throw new IllegalArgumentException("Device ID is required");
        }
    }
}
