package com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class ClientLinkNotFoundException extends AgroSafeException {

    public ClientLinkNotFoundException(Long clientId) {
        super("Client link not found with ID: " + clientId);
    }

    public ClientLinkNotFoundException(String message) {
        super(message);
    }
}
