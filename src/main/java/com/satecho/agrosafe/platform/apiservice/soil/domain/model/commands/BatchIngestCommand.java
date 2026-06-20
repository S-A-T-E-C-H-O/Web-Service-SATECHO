package com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands;

import java.util.List;

public record BatchIngestCommand(List<IngestTelemetryCommand> readings) {
    public BatchIngestCommand {
        if (readings == null || readings.isEmpty()) throw new IllegalArgumentException("Batch readings must not be null or empty");
    }
}
