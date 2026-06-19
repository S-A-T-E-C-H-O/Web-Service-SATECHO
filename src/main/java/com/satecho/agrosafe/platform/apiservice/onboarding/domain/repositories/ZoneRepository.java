package com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;

import java.util.List;
import java.util.Optional;

public interface ZoneRepository {
    Optional<IrrigationZone> findById(Long id);
    List<IrrigationZone> findAllByFarmId(Long farmId);
    IrrigationZone save(IrrigationZone zone);
}