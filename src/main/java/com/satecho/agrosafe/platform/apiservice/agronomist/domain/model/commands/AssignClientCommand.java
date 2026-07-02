package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands;

public record AssignClientCommand(Long agronomistUserId, Long farmerUserId) {
}
