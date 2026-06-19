package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.ScheduleQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.IrrigationScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ScheduleQueryServiceImpl implements ScheduleQueryService {

    private final IrrigationScheduleRepository scheduleRepository;

    public ScheduleQueryServiceImpl(IrrigationScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Optional<IrrigationSchedule> handle(GetScheduleByIdQuery query) {
        return scheduleRepository.findByIdAndZoneId(query.scheduleId(), query.zoneId());
    }

    @Override
    public List<IrrigationSchedule> handle(GetSchedulesByZoneQuery query) {
        return scheduleRepository.findByZoneIdOrderByCreatedAtDesc(query.zoneId());
    }
}
