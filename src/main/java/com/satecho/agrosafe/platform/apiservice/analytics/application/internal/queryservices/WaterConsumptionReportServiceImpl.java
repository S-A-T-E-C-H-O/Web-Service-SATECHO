package com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.WaterConsumptionReportService;
import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.IrrigationQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetSessionHistoryByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class WaterConsumptionReportServiceImpl implements WaterConsumptionReportService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneOffset.UTC);

    private final IrrigationQueryService irrigationQueryService;
    private final ZoneQueryService zoneQueryService;

    public WaterConsumptionReportServiceImpl(IrrigationQueryService irrigationQueryService, ZoneQueryService zoneQueryService) {
        this.irrigationQueryService = irrigationQueryService;
        this.zoneQueryService = zoneQueryService;
    }

    @Override
    public byte[] generatePdf(Long zoneId, Instant fromDate, Instant toDate) {
        var zone = zoneQueryService.findById(zoneId).orElse(null);
        var sessions = irrigationQueryService.handle(new GetSessionHistoryByZoneQuery(zoneId, fromDate, toDate, 100));
        double totalLiters = sessions.stream()
                .filter(s -> s.getTotalWaterUsedLiters() != null)
                .mapToDouble(s -> s.getTotalWaterUsedLiters()).sum();

        try {
            var out = new ByteArrayOutputStream();
            var document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter.getInstance(document, out);
            document.open();

            var titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            var normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
            var boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);

            document.add(new Paragraph("Water Consumption Report", titleFont));
            document.add(new Paragraph("Zone: " + (zone != null ? zone.getName() : "Zone #" + zoneId), normalFont));
            document.add(new Paragraph("Period: " + FORMATTER.format(fromDate) + " to " + FORMATTER.format(toDate), normalFont));
            document.add(new Paragraph("Total water used: " + String.format("%.2f", totalLiters) + " L", boldFont));
            document.add(Chunk.NEWLINE);

            var table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell(new Phrase("Started", boldFont));
            table.addCell(new Phrase("Duration (min)", boldFont));
            table.addCell(new Phrase("Water used (L)", boldFont));
            table.addCell(new Phrase("Status", boldFont));
            for (var session : sessions) {
                table.addCell(new Phrase(session.getStartedAt() != null ? FORMATTER.format(session.getStartedAt()) : "-", normalFont));
                table.addCell(new Phrase(session.getDurationMinutes() != null ? session.getDurationMinutes().toString() : "-", normalFont));
                table.addCell(new Phrase(session.getTotalWaterUsedLiters() != null ? String.format("%.2f", session.getTotalWaterUsedLiters()) : "-", normalFont));
                table.addCell(new Phrase(session.getStatus() != null ? session.getStatus().name() : "-", normalFont));
            }
            document.add(table);
            document.close();
            return out.toByteArray();
        } catch (DocumentException e) {
            throw new IllegalStateException("Failed to generate water consumption report PDF", e);
        }
    }
}
