package com.satecho.agrosafe.platform.apiservice.security.domain.model.commands;

public record AcknowledgeSecurityEventCommand(Long eventId, Long acknowledgedBy) {
    public AcknowledgeSecurityEventCommand {
        if (eventId == null) throw new IllegalArgumentException("eventId cannot be null");
        if (acknowledgedBy == null) throw new IllegalArgumentException("acknowledgedBy cannot be null");
    }
}
