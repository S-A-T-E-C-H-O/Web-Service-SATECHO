package com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices.DashboardQueryService;
import com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries.FarmerDashboard;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import org.springframework.stereotype.Service;

@Service
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final DeviceQueryService deviceQueryService;

    public DashboardQueryServiceImpl(FarmQueryService farmQueryService, ZoneQueryService zoneQueryService,
                                      DeviceQueryService deviceQueryService) {
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.deviceQueryService = deviceQueryService;
    }

    @Override
    public FarmerDashboard getFarmerDashboard(Long farmerUserId) {
        var farms = farmQueryService.findAllByUserId(farmerUserId);
        int totalZones = farms.stream().mapToInt(f -> zoneQueryService.findAllByFarmId(f.getId()).size()).sum();

        var devices = deviceQueryService.handle(new GetAllDevicesByUserIdQuery(farmerUserId, null, null));
        int online = (int) devices.stream().filter(d -> Boolean.TRUE.equals(d.getOnline())).count();
        int offline = devices.size() - online;
        int errorCount = (int) devices.stream()
                .filter(d -> d.getHealthStatus() != null && !"HEALTHY".equals(d.getHealthStatus())).count();

        var farmSummaries = farms.stream()
                .map(f -> new FarmerDashboard.FarmSummary(f.getId(), f.getName(), f.getLocation(),
                        f.getCropType() != null ? f.getCropType().name() : null, f.getHectares()))
                .toList();

        return new FarmerDashboard(totalZones, online, offline, errorCount, farmSummaries);
    }
}
