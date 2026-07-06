package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

/**
 * Command record for completing a scheduled field visit.
 *
 * @param visitId         the unique identifier of the field visit to be completed
 * @param currentUserId   the unique identifier of the user completing the visit
 * @param latitude        the latitude where the completion is recorded
 * @param longitude       the longitude where the completion is recorded
 * @param photoBase64     the base64 encoded photo captured during the visit
 * @author Colegio
 * @version 1.0
 */
public record CompleteFieldVisitCommand(Long visitId, Long currentUserId, Double latitude, Double longitude,
                                         String photoBase64) {
}
