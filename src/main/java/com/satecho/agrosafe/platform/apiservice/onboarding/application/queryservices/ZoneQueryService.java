package com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;

import java.util.List;
import java.util.Optional;

public interface ZoneQueryService {
    Optional<IrrigationZone> findById(Long zoneId);
    Optional<IrrigationZone> findByDeviceId(Long deviceId);
    List<IrrigationZone> findAllByFarmId(Long farmId);
}