package com.satecho.agrosafe.platform.apiservice.onboarding.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class ZoneNotFoundException extends AgroSafeException {
    public ZoneNotFoundException(Long zoneId) {
        super("Irrigation zone not found with ID: " + zoneId);
    }
}