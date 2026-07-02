package com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands;

public record RegisterDeviceTokenCommand(Long userId, String fcmToken, String platform) {
    public RegisterDeviceTokenCommand {
        if (userId == null) throw new IllegalArgumentException("userId cannot be null");
        if (fcmToken == null || fcmToken.isBlank()) throw new IllegalArgumentException("fcmToken cannot be null or blank");
    }
}
