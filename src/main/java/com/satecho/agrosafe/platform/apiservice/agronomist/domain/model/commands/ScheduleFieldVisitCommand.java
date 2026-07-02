package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

import java.time.Instant;

public record ScheduleFieldVisitCommand(Long agronomistUserId, Long farmId, Instant scheduledAt, String tag,
                                         String noteTitle, String noteBody, Boolean urgent,
                                         Double latitude, Double longitude, String photoBase64) {
}
