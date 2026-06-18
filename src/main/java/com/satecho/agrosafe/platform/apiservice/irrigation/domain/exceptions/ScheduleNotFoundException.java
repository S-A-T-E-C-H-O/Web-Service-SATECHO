package com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions;

import com.satecho.agrosafe.platform.shared.domain.exception.AgroSafeException;

public class ScheduleNotFoundException extends AgroSafeException {
    public ScheduleNotFoundException(Long scheduleId) { super("Irrigation schedule not found with ID: " + scheduleId); }
}
