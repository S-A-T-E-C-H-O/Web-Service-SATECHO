package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources;

public record CreateRecommendationResource(Long farmerId, Long zoneId, String title, String description,
                                            String recommendedActions, String attachmentUrl, String priority) {
}
