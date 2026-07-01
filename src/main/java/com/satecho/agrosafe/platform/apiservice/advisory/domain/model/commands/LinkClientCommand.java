package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands;

public record LinkClientCommand(
        Long agronomistId,
        Long farmerId,
        String notes
) {

    public LinkClientCommand {
        if (agronomistId == null) {
            throw new IllegalArgumentException("Agronomist ID is required");
        }
        if (farmerId == null) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
    }
}
