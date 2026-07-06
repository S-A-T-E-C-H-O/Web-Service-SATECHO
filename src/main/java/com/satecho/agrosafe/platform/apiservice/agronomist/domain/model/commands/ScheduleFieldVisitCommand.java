package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

import java.time.Instant;

/**
 * Command record for scheduling a new field visit.
 *
 * @param agronomistUserId the unique identifier of the agronomist user
 * @param farmId           the unique identifier of the farm to visit
 * @param scheduledAt      the scheduled timestamp of the visit
 * @param tag              the tag or category of the visit
 * @param noteTitle        the title of the visit note
 * @param noteBody         the body of the visit note
 * @param urgent           flag indicating if the visit is urgent
 * @param latitude         the latitude coordinate of the visit
 * @param longitude        the longitude coordinate of the visit
 * @param photoBase64      the base64 encoded photo to associate with the visit
 * @author Colegio
 * @version 1.0
 */
public record ScheduleFieldVisitCommand(Long agronomistUserId, Long farmId, Instant scheduledAt, String tag,
                                         String noteTitle, String noteBody, Boolean urgent,
                                         Double latitude, Double longitude, String photoBase64) {
}
