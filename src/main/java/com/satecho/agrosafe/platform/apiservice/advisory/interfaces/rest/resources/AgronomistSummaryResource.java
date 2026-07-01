package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources;

public record AgronomistSummaryResource(
        int totalClients,
        int activeClients,
        long totalRecommendations
) {
}
