package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/**
 * Resource class representing the request to complete a field visit.
 * Contains GPS coordinates and base64 encoded photo confirmation.
 *
 * @param latitude the latitude coordinate of the completion location
 * @param longitude the longitude coordinate of the completion location
 * @param photoBase64 the base64 encoded photo confirming the visit
 */
public record CompleteFieldVisitResource(Double latitude, Double longitude, String photoBase64) {
}

