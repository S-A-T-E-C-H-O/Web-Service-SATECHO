package com.satecho.agrosafe.platform.apiservice.onboarding.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class FarmNotFoundException extends AgroSafeException {
    public FarmNotFoundException(Long farmId) {
        super("Farm not found with ID: " + farmId);
    }
}