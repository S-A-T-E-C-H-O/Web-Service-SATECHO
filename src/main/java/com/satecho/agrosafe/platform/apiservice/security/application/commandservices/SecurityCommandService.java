package com.satecho.agrosafe.platform.apiservice.security.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface SecurityCommandService {
    Result<SecurityEvent, ApplicationError> ingestSecurityEvent(IngestSecurityEventCommand command);
    Result<SecurityEvent, ApplicationError> acknowledgeSecurityEvent(AcknowledgeSecurityEventCommand command);
}
