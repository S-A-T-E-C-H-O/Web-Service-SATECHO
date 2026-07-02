package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources;

public record RegisterDeviceTokenResource(String fcmToken, String platform) {
}