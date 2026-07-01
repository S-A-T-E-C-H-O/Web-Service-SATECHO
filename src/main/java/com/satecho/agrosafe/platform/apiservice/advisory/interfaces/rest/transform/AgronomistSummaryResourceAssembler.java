package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.AgronomistSummaryResource;

public class AgronomistSummaryResourceAssembler {

    private AgronomistSummaryResourceAssembler() {
    }

    public static AgronomistSummaryResource toResource(int totalClients, int activeClients, long totalRecommendations) {
        return new AgronomistSummaryResource(totalClients, activeClients, totalRecommendations);
    }
}
