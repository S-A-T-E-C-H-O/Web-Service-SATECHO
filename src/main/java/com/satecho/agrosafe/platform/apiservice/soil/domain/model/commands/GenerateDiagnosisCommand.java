package com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands;

public record GenerateDiagnosisCommand(Long zoneId) {
    public GenerateDiagnosisCommand {
        if (zoneId == null) throw new IllegalArgumentException("Zone ID is required");
    }
}
