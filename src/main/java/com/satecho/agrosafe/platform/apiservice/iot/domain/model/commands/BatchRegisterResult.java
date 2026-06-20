package com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands;

import java.util.List;

public record BatchRegisterResult(int totalSubmitted, int succeeded, int failed, List<Entry> entries) {
    public record Entry(String serialNumber, String status, Long deviceId, String error) {}
}
