package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands;

public record UnlinkClientCommand(
        Long clientId,
        Long agronomistId
) {

    public UnlinkClientCommand {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        if (agronomistId == null) {
            throw new IllegalArgumentException("Agronomist ID is required");
        }
    }
}
