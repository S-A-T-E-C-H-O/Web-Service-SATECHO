package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource representing the details of a field visit.
 * Field names match Mobile App's FieldVisitRepositoryImpl._toVisit() exactly.
 *
 * @param id the unique identifier of the field visit
 * @param farmId the unique identifier of the farm being visited
 * @param farmName the name of the farm
 * @param ownerName the name of the farm owner
 * @param scheduledAt the scheduled date and time of the visit (ISO format or similar string)
 * @param tag a tag classifying the visit
 * @param noteTitle the title of the visit note/report
 * @param noteBody the body/description of the visit note/report
 * @param urgent indicates if the visit is marked as urgent
 * @param completed indicates if the visit has been completed
 * @param latitude the latitude coordinate of the completion check-in
 * @param longitude the longitude coordinate of the completion check-in
 * @param photoBase64 the base64 encoded photo confirming the visit
 */
public record FieldVisitResource(Long id, Long farmId, String farmName, String ownerName, String scheduledAt,
                                  String tag, String noteTitle, String noteBody, Boolean urgent, Boolean completed,
                                  Double latitude, Double longitude, String photoBase64) {
}

