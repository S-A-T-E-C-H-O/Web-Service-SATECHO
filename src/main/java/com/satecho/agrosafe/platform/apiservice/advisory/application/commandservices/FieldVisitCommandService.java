package com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.CompleteVisitCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.ScheduleVisitCommand;

public interface FieldVisitCommandService {
    FieldVisit schedule(ScheduleVisitCommand command);
    FieldVisit complete(CompleteVisitCommand command);
}
