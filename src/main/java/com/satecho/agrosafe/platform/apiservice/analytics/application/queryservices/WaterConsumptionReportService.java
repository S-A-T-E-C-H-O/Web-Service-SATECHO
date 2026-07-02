package com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices;

import java.time.Instant;

public interface WaterConsumptionReportService {
    /** Renders a PDF summarizing irrigation sessions and total water used for a zone over a date range (EP-013-US001). */
    byte[] generatePdf(Long zoneId, Instant fromDate, Instant toDate);
}
