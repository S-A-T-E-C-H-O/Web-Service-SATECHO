package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources;

public record UpdateSecuritySettingsResource(Integer motionSensitivity, String alertMode,
                                             String detectionScheduleStart, String detectionScheduleEnd,
                                             String notificationContacts) {}
