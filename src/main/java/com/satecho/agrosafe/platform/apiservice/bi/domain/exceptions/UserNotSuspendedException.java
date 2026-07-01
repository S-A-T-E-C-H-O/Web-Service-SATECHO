package com.satecho.agrosafe.platform.apiservice.bi.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class UserNotSuspendedException extends AgroSafeException {

    public UserNotSuspendedException(Long userId) {
        super("User not currently suspended with ID: " + userId);
    }

    public UserNotSuspendedException(String message) {
        super(message);
    }
}
