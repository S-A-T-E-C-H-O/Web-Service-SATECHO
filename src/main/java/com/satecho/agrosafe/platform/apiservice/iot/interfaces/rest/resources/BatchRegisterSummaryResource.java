package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import java.util.List;

public record BatchRegisterSummaryResource(int totalSubmitted, int succeeded, int failed, List<BatchEntryResource> entries) {
    public record BatchEntryResource(String serialNumber, String status, Long deviceId, String error) {}
}
