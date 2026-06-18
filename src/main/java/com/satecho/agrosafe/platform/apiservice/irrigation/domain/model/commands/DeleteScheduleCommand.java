package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands;

public record DeleteScheduleCommand(Long scheduleId, Long zoneId) {}
