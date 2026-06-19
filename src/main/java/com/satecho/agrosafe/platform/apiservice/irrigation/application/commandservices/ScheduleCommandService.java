package com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface ScheduleCommandService {
    Result<IrrigationSchedule, ApplicationError> createSchedule(CreateScheduleCommand command);
    Result<IrrigationSchedule, ApplicationError> updateSchedule(UpdateScheduleCommand command);
    Result<Void, ApplicationError> deleteSchedule(DeleteScheduleCommand command);
}
