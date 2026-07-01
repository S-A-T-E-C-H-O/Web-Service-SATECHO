package com.satecho.agrosafe.platform.apiservice.bi.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class FirmwareNotFoundException extends AgroSafeException {

    public FirmwareNotFoundException(Long firmwareId) {
        super("Firmware release not found with ID: " + firmwareId);
    }

    public FirmwareNotFoundException(String message) {
        super(message);
    }
}
