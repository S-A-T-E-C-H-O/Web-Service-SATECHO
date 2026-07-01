package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries;

public record GetClientsByAgronomistQuery(
        Long agronomistId
) {

    public GetClientsByAgronomistQuery {
        if (agronomistId == null) {
            throw new IllegalArgumentException("Agronomist ID is required");
        }
    }
}
