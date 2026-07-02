package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

public record ScheduleFieldVisitResource(Long farmId, String scheduledAt, String tag, String noteTitle,
                                          String noteBody, Boolean urgent,
                                          Double latitude, Double longitude, String photoBase64) {
}
