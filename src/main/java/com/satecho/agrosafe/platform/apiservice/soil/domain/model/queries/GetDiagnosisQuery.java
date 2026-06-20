package com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries;

public record GetDiagnosisQuery(Long diagnosisId) {
    public GetDiagnosisQuery { if (diagnosisId == null) throw new IllegalArgumentException("Diagnosis ID is required"); }
}
