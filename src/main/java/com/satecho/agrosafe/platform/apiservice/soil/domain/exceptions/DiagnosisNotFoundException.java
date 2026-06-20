package com.satecho.agrosafe.platform.apiservice.soil.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class DiagnosisNotFoundException extends AgroSafeException {
    public DiagnosisNotFoundException(Long diagnosisId) { super("Diagnosis not found with ID: " + diagnosisId); }
    public DiagnosisNotFoundException(String message) { super(message); }
}
