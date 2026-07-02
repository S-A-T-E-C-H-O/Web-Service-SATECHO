package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

public record CompleteFieldVisitCommand(Long visitId, Long currentUserId, Double latitude, Double longitude,
                                         String photoBase64) {
}
