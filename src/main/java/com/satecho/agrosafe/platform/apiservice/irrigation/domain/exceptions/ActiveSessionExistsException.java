package com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class ActiveSessionExistsException extends AgroSafeException {
    public ActiveSessionExistsException(Long zoneId) { super("An active irrigation session already exists for zone: " + zoneId); }
}
