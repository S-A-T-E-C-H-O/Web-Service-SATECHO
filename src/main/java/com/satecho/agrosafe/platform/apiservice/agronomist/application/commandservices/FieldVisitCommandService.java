package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.ScheduleFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface FieldVisitCommandService {
    Result<FieldVisit, ApplicationError> scheduleVisit(ScheduleFieldVisitCommand command);
    Result<FieldVisit, ApplicationError> completeVisit(Long visitId, Long currentUserId);
}
