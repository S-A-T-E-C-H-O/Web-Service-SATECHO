package com.satecho.agrosafe.platform.apiservice.activitylog.interfaces.rest.resources;

import java.time.Instant;

public record ActivityLogEntryResource(Long id, Long farmId, String type, String description, Instant occurredAt) {
}
