package com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AlertQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.DashboardQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries.FarmerDashboard;
import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.IrrigationQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetSessionHistoryByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetReadingsByTimeRangeQuery;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final DeviceQueryService deviceQueryService;
    private final TelemetryQueryService telemetryQueryService;
    private final IrrigationQueryService irrigationQueryService;
    private final AlertQueryService alertQueryService;

    public DashboardQueryServiceImpl(FarmQueryService farmQueryService, ZoneQueryService zoneQueryService,
                                      DeviceQueryService deviceQueryService, TelemetryQueryService telemetryQueryService,
                                      IrrigationQueryService irrigationQueryService, AlertQueryService alertQueryService) {
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.deviceQueryService = deviceQueryService;
        this.telemetryQueryService = telemetryQueryService;
        this.irrigationQueryService = irrigationQueryService;
        this.alertQueryService = alertQueryService;
    }

    @Override
    public FarmerDashboard getFarmerDashboard(Long farmerUserId) {
        var farms = farmQueryService.findAllByUserId(farmerUserId);
        var allZones = farms.stream().flatMap(f -> zoneQueryService.findAllByFarmId(f.getId()).stream()).toList();
        int totalZones = allZones.size();

        var devices = deviceQueryService.handle(new GetAllDevicesByUserIdQuery(farmerUserId, null, null));
        int online = (int) devices.stream().filter(d -> Boolean.TRUE.equals(d.getOnline())).count();
        int offline = devices.size() - online;
        int errorCount = (int) devices.stream()
                .filter(d -> d.getHealthStatus() != null && !"HEALTHY".equals(d.getHealthStatus())).count();

        var farmSummaries = farms.stream()
                .map(f -> new FarmerDashboard.FarmSummary(f.getId(), f.getName(), f.getLocation(),
                        f.getCropType() != null ? f.getCropType().name() : null, f.getHectares()))
                .toList();

        Instant weekAgo = Instant.now().minus(7, ChronoUnit.DAYS);

        // EP-012-US023: 7-day average moisture/EC across every zone the farmer owns.
        List<Double> moistureReadings = new ArrayList<>();
        List<Double> ecReadings = new ArrayList<>();
        double weeklyIrrigationMinutes = 0;
        for (var zone : allZones) {
            telemetryQueryService.getReadingsByTimeRange(
                    new GetReadingsByTimeRangeQuery(zone.getId(), weekAgo, Instant.now(), MetricType.SOIL_MOISTURE))
                    .forEach(r -> moistureReadings.add(r.getValue()));
            telemetryQueryService.getReadingsByTimeRange(
                    new GetReadingsByTimeRangeQuery(zone.getId(), weekAgo, Instant.now(), MetricType.ELECTRICAL_CONDUCTIVITY))
                    .forEach(r -> ecReadings.add(r.getValue()));
            var sessions = irrigationQueryService.handle(
                    new GetSessionHistoryByZoneQuery(zone.getId(), weekAgo, Instant.now(), 100));
            for (var session : sessions) {
                if (session.getDurationMinutes() != null) weeklyIrrigationMinutes += session.getDurationMinutes();
            }
        }
        Double avgMoisture = moistureReadings.isEmpty() ? null
                : moistureReadings.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        Double avgEc = ecReadings.isEmpty() ? null
                : ecReadings.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        int activeAlertCount = farms.stream()
                .mapToInt(f -> alertQueryService.findActiveByFarmId(f.getId()).size())
                .sum();

        boolean criticalMoisture = avgMoisture != null && avgMoisture < 25.0;

        return new FarmerDashboard(totalZones, online, offline, errorCount, farmSummaries, avgMoisture, avgEc,
                weeklyIrrigationMinutes / 60.0, activeAlertCount, criticalMoisture);
    }
}
