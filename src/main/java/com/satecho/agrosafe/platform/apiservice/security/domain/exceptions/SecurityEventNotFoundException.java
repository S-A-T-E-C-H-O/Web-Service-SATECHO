package com.satecho.agrosafe.platform.apiservice.security.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class SecurityEventNotFoundException extends AgroSafeException {
    public SecurityEventNotFoundException(Long eventId) { super("Security event not found with id: " + eventId); }
    public SecurityEventNotFoundException(String message) { super(message); }
}
