package com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands;

public record ResetPasswordCommand(String token, String newPassword) {
}
