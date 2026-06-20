package com.satecho.agrosafe.platform.apiservice.soil.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class InvalidReadingException extends AgroSafeException {
    public InvalidReadingException(String message) { super(message); }
    public InvalidReadingException(String message, Throwable cause) { super(message, cause); }
}
