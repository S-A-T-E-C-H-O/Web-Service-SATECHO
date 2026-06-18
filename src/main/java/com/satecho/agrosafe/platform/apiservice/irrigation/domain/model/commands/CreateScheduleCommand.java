package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.RecurrencePattern;
import java.time.Instant;

public record CreateScheduleCommand(Long zoneId, Long deviceId, Instant startAt, Integer durationMinutes,
                                    RecurrencePattern recurrence, String cronExpression, Boolean enabled) {}
