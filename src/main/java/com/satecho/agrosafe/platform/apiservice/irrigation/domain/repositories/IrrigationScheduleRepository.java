package com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSchedule;

import java.util.List;
import java.util.Optional;

public interface IrrigationScheduleRepository {
    IrrigationSchedule save(IrrigationSchedule schedule);
    void delete(IrrigationSchedule schedule);
    Optional<IrrigationSchedule> findByIdAndZoneId(Long id, Long zoneId);
    List<IrrigationSchedule> findByZoneIdOrderByCreatedAtDesc(Long zoneId);
}
