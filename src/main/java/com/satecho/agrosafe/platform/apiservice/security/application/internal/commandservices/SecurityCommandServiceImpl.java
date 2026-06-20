package com.satecho.agrosafe.platform.apiservice.security.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.domain.exceptions.SecurityEventNotFoundException;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecurityEventRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityCommandServiceImpl implements SecurityCommandService {

    private final SecurityEventRepository securityEventRepository;

    public SecurityCommandServiceImpl(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @Override
    public Result<SecurityEvent, ApplicationError> ingestSecurityEvent(IngestSecurityEventCommand command) {
        var event = new SecurityEvent(command.farmId(), command.deviceId(), command.classification(),
                command.confidenceLevel(), command.detectedAt(), command.locationDescription(), command.rawData());
        return Result.success(securityEventRepository.save(event));
    }

    @Override
    public Result<SecurityEvent, ApplicationError> acknowledgeSecurityEvent(AcknowledgeSecurityEventCommand command) {
        var event = securityEventRepository.findById(command.eventId())
                .orElseThrow(() -> new SecurityEventNotFoundException(command.eventId()));
        event.acknowledge(command.acknowledgedBy());
        return Result.success(securityEventRepository.save(event));
    }
}
