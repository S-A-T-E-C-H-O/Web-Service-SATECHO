package com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class NoActiveSessionException extends AgroSafeException {
    public NoActiveSessionException(Long zoneId) { super("No active irrigation session found for zone: " + zoneId); }
}
