package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices.ScheduleCommandService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions.ScheduleNotFoundException;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.IrrigationScheduleRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduleCommandServiceImpl implements ScheduleCommandService {

    private final IrrigationScheduleRepository scheduleRepository;

    public ScheduleCommandServiceImpl(IrrigationScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Result<IrrigationSchedule, ApplicationError> createSchedule(CreateScheduleCommand command) {
        var schedule = new IrrigationSchedule(command.zoneId(), command.deviceId(), command.startAt(),
                command.durationMinutes(), command.recurrence(), command.cronExpression(), command.enabled());
        return Result.success(scheduleRepository.save(schedule));
    }

    @Override
    public Result<IrrigationSchedule, ApplicationError> updateSchedule(UpdateScheduleCommand command) {
        var schedule = scheduleRepository.findByIdAndZoneId(command.scheduleId(), command.zoneId())
                .orElseThrow(() -> new ScheduleNotFoundException(command.scheduleId()));
        schedule.update(command.startAt(), command.durationMinutes(), command.recurrence(), command.cronExpression(), command.enabled());
        return Result.success(scheduleRepository.save(schedule));
    }

    @Override
    public Result<Void, ApplicationError> deleteSchedule(DeleteScheduleCommand command) {
        var schedule = scheduleRepository.findByIdAndZoneId(command.scheduleId(), command.zoneId())
                .orElseThrow(() -> new ScheduleNotFoundException(command.scheduleId()));
        scheduleRepository.delete(schedule);
        return Result.success(null);
    }
}
