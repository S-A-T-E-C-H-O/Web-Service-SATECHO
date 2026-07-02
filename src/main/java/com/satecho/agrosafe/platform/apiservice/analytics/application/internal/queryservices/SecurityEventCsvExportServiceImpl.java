package com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.SecurityEventCsvExportService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SecurityEventCsvExportServiceImpl implements SecurityEventCsvExportService {

    private static final String[] HEADER = {"id", "deviceId", "classification", "severity", "confidenceLevel",
            "detectedAt", "acknowledged", "acknowledgedBy", "acknowledgedAt", "locationDescription"};

    @Override
    public byte[] toCsv(List<SecurityEvent> events) {
        var out = new ByteArrayOutputStream();
        try (var writer = new CSVWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            writer.writeNext(HEADER);
            for (var e : events) {
                writer.writeNext(new String[]{
                        String.valueOf(e.getId()),
                        String.valueOf(e.getDeviceId()),
                        e.getClassification() != null ? e.getClassification().name() : "",
                        e.getSeverity() != null ? e.getSeverity().name() : "",
                        e.getConfidenceLevel() != null ? e.getConfidenceLevel().toString() : "",
                        e.getDetectedAt() != null ? e.getDetectedAt().toString() : "",
                        String.valueOf(Boolean.TRUE.equals(e.getAcknowledged())),
                        e.getAcknowledgedBy() != null ? e.getAcknowledgedBy().toString() : "",
                        e.getAcknowledgedAt() != null ? e.getAcknowledgedAt().toString() : "",
                        e.getLocationDescription() != null ? e.getLocationDescription() : ""
                });
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate security events CSV export", ex);
        }
        return out.toByteArray();
    }
}
