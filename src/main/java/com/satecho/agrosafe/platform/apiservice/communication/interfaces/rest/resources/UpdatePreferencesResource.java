package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources;

import java.util.List;

public record UpdatePreferencesResource(String notificationType, List<String> channelsEnabled,
                                        Boolean dailyDigestEnabled, String quietHoursStart, String quietHoursEnd) {}