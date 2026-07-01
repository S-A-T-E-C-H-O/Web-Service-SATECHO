package com.satecho.agrosafe.platform.apiservice.bi.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class DiagnosticNotFoundException extends AgroSafeException {

    public DiagnosticNotFoundException(Long diagnosticId) {
        super("Diagnostic session not found with ID: " + diagnosticId);
    }

    public DiagnosticNotFoundException(String message) {
        super(message);
    }
}
