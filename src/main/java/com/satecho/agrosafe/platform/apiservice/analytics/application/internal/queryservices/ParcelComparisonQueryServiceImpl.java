package com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.ParcelComparisonQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries.ParcelComparison;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetLatestReadingsByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParcelComparisonQueryServiceImpl implements ParcelComparisonQueryService {

    private static final int MAX_PARCELS = 4;

    private final ZoneQueryService zoneQueryService;
    private final TelemetryQueryService telemetryQueryService;

    public ParcelComparisonQueryServiceImpl(ZoneQueryService zoneQueryService, TelemetryQueryService telemetryQueryService) {
        this.zoneQueryService = zoneQueryService;
        this.telemetryQueryService = telemetryQueryService;
    }

    @Override
    public Result<List<ParcelComparison>, ApplicationError> compare(List<Long> zoneIds) {
        if (zoneIds == null || zoneIds.isEmpty()) {
            return Result.failure(ApplicationError.validationError("zoneIds", "At least one zone id is required"));
        }
        if (zoneIds.size() > MAX_PARCELS) {
            return Result.failure(ApplicationError.validationError("zoneIds", "At most " + MAX_PARCELS + " parcels can be compared at once"));
        }

        List<ParcelComparison> comparisons = new ArrayList<>();
        for (Long zoneId : zoneIds) {
            var zone = zoneQueryService.findById(zoneId);
            if (zone.isEmpty()) {
                return Result.failure(ApplicationError.notFound("Zone", String.valueOf(zoneId)));
            }
            var z = zone.get();
            Double moisture = null, temperature = null, ec = null;
            for (var reading : telemetryQueryService.getLatestReadingsByZone(new GetLatestReadingsByZoneQuery(zoneId))) {
                if (reading.getMetricType() == MetricType.SOIL_MOISTURE) moisture = reading.getValue();
                if (reading.getMetricType() == MetricType.SOIL_TEMPERATURE) temperature = reading.getValue();
                if (reading.getMetricType() == MetricType.ELECTRICAL_CONDUCTIVITY) ec = reading.getValue();
            }
            comparisons.add(new ParcelComparison(zoneId, z.getName(), moisture, temperature, ec,
                    z.getAreaHectares(), z.getCropType() != null ? z.getCropType().name() : null));
        }
        return Result.success(comparisons);
    }
}
