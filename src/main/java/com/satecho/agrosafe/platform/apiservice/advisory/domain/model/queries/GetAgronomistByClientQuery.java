package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries;

public record GetAgronomistByClientQuery(
        Long farmerId
) {

    public GetAgronomistByClientQuery {
        if (farmerId == null) {
            throw new IllegalArgumentException("Farmer ID is required");
        }
    }
}
