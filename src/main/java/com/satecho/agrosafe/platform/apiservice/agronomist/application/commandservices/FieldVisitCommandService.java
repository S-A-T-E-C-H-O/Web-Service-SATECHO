package com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CompleteFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.ScheduleFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

/**
 * Service interface for handling command operations related to field visits.
 */
public interface FieldVisitCommandService {
    /**
     * Schedules a new field visit.
     *
     * @param command the command containing scheduling details
     * @return a Result containing the scheduled FieldVisit if successful, or an ApplicationError if not
     */
    Result<FieldVisit, ApplicationError> scheduleVisit(ScheduleFieldVisitCommand command);

    /**
     * Completes an existing field visit.
     *
     * @param command the command containing completion details
     * @return a Result containing the completed FieldVisit if successful, or an ApplicationError if not
     */
    Result<FieldVisit, ApplicationError> completeVisit(CompleteFieldVisitCommand command);
}
