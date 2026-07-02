package com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands;

public record ChangePasswordCommand(Long userId, String currentPassword, String newPassword) {
}
