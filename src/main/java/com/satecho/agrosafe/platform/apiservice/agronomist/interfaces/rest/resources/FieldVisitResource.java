package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

/** Field names match Mobile App's FieldVisitRepositoryImpl._toVisit() exactly. */
public record FieldVisitResource(Long id, Long farmId, String farmName, String ownerName, String scheduledAt,
                                  String tag, String noteTitle, String noteBody, Boolean urgent) {
}
