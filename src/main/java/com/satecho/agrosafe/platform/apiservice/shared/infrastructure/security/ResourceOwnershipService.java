package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetDeviceByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import org.springframework.stereotype.Component;

/**
 * Centralizes the "does the current user own this resource, or hold ROLE_ADMIN,
 * or is an agronomist assigned to the owning farmer" check for farm/zone/device-scoped
 * endpoints. Every controller that accepts a farmId/zoneId/deviceId path variable must
 * call one of these before touching the resource — path variables are not implicitly trusted.
 */
@Component
public class ResourceOwnershipService {

    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final DeviceQueryService deviceQueryService;
    private final ClientAssignmentRepository clientAssignmentRepository;

    public ResourceOwnershipService(FarmQueryService farmQueryService, ZoneQueryService zoneQueryService,
                                     DeviceQueryService deviceQueryService,
                                     ClientAssignmentRepository clientAssignmentRepository) {
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.deviceQueryService = deviceQueryService;
        this.clientAssignmentRepository = clientAssignmentRepository;
    }

    public boolean isFarmOwnerOrAdmin(Long farmId) {
        if (SecurityContextUtil.isAdmin()) return true;
        if (farmId == null) return false;
        return farmQueryService.findById(farmId)
                .map(farm -> {
                    Long currentUserId = SecurityContextUtil.getCurrentUserId();
                    if (farm.getUserId().equals(currentUserId)) return true;
                    return clientAssignmentRepository
                            .existsByAgronomistUserIdAndFarmerUserId(currentUserId, farm.getUserId());
                })
                .orElse(false);
    }

    /** Resolves the zone's owning farm and applies the same ownership rule. */
    public boolean isZoneOwnerOrAdmin(Long zoneId) {
        if (SecurityContextUtil.isAdmin()) return true;
        if (zoneId == null) return false;
        return zoneQueryService.findById(zoneId)
                .map(zone -> isFarmOwnerOrAdmin(zone.getFarmId()))
                .orElse(false);
    }

    public boolean isDeviceOwnerOrAdmin(Long deviceId) {
        if (SecurityContextUtil.isAdmin()) return true;
        if (deviceId == null) return false;
        return deviceQueryService.handle(new GetDeviceByIdQuery(deviceId))
                .map(device -> {
                    Long currentUserId = SecurityContextUtil.getCurrentUserId();
                    if (device.getUserId().equals(currentUserId)) return true;
                    return clientAssignmentRepository
                            .existsByAgronomistUserIdAndFarmerUserId(currentUserId, device.getUserId());
                })
                .orElse(false);
    }
}
