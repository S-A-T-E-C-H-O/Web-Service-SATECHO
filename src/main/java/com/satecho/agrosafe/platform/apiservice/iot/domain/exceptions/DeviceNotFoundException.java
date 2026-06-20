package com.satecho.agrosafe.platform.apiservice.iot.domain.exceptions;

import com.satecho.agrosafe.platform.shared.domain.exception.AgroSafeException;

public class DeviceNotFoundException extends AgroSafeException {
    public DeviceNotFoundException(Long deviceId) { super("Device not found with id: " + deviceId); }
    public DeviceNotFoundException(String serialNumber) { super("Device not found with serial number: " + serialNumber); }
}
