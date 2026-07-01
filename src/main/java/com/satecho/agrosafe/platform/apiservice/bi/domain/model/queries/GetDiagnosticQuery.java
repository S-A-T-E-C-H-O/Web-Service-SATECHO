package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetDiagnosticQuery(
        Long diagnosticId
) {
    public GetDiagnosticQuery {
        if (diagnosticId == null) {
            throw new IllegalArgumentException("Diagnostic ID is required");
        }
    }
}
