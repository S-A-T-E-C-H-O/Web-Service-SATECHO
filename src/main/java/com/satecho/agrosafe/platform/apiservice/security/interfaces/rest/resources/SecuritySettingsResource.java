package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources;

public record SecuritySettingsResource(Long id, Long farmId, Integer motionSensitivity, String alertMode,
                                       String detectionScheduleStart, String detectionScheduleEnd,
                                       String notificationContacts, java.util.Set<Long> disabledZoneIds) {}
