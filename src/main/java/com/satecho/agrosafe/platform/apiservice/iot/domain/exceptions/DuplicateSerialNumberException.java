package com.satecho.agrosafe.platform.apiservice.iot.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class DuplicateSerialNumberException extends AgroSafeException {
    public DuplicateSerialNumberException(String serialNumber) { super("Device with serial number already exists: " + serialNumber); }
}
