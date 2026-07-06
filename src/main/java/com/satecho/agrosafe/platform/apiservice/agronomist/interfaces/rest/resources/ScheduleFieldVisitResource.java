package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource class representing the request to schedule a field visit.
 *
 * @param farmId the unique identifier of the farm to be visited
 * @param scheduledAt the scheduled date and time of the visit (ISO format or similar string)
 * @param tag a tag classifying the visit
 * @param noteTitle the title of the visit note/report
 * @param noteBody the body/description of the visit note/report
 * @param urgent indicates if the visit is marked as urgent
 * @param latitude the latitude coordinate of the completion check-in
 * @param longitude the longitude coordinate of the completion check-in
 * @param photoBase64 the base64 encoded photo confirming the visit
 */
public record ScheduleFieldVisitResource(Long farmId, String scheduledAt, String tag, String noteTitle,
                                          String noteBody, Boolean urgent,
                                          Double latitude, Double longitude, String photoBase64) {
}

