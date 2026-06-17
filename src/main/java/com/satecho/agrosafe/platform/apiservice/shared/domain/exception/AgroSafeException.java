package com.satecho.agrosafe.platform.apiservice.shared.domain.exception;

public class AgroSafeException extends RuntimeException {

    public AgroSafeException(String message) {
        super(message);
    }

    public AgroSafeException(String message, Throwable cause) {
        super(message, cause);
    }
}