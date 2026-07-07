package com.satecho.agrosafe.platform.apiservice.security.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.domain.exceptions.SecurityEventNotFoundException;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.events.IntrusionDetectedEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecurityEventRepository;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecuritySettingsRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecurityCommandServiceImpl implements SecurityCommandService {

    private final SecurityEventRepository securityEventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ZoneQueryService zoneQueryService;
    private final SecuritySettingsRepository securitySettingsRepository;

    public SecurityCommandServiceImpl(SecurityEventRepository securityEventRepository, ApplicationEventPublisher eventPublisher,
                                       ZoneQueryService zoneQueryService, SecuritySettingsRepository securitySettingsRepository) {
        this.securityEventRepository = securityEventRepository;
        this.eventPublisher = eventPublisher;
        this.zoneQueryService = zoneQueryService;
        this.securitySettingsRepository = securitySettingsRepository;
    }

    @Override
    public Result<SecurityEvent, ApplicationError> ingestSecurityEvent(IngestSecurityEventCommand command) {
        var event = new SecurityEvent(command.farmId(), command.deviceId(), command.zoneId(), command.classification(),
                command.confidenceLevel(), command.detectedAt(), command.locationDescription(), command.rawData(),
                command.pulseDurationMs(), command.triggersPerMinute());
        var saved = securityEventRepository.save(event);
        // EP-013: the raw event is always kept for the audit trail, but if the farmer silenced
        // detection for this specific parcel, skip the notification fan-out.
        if (isZoneDetectionEnabled(command)) {
            eventPublisher.publishEvent(new IntrusionDetectedEvent(saved.getId(), saved.getFarmId(), saved.getDeviceId(),
                    saved.getClassification(), saved.getSeverity(), saved.getConfidenceLevel(), saved.getDetectedAt(),
                    saved.getLocationDescription()));
        }
        return Result.success(saved);
    }

    private boolean isZoneDetectionEnabled(IngestSecurityEventCommand command) {
        var zoneId = zoneQueryService.findByDeviceId(command.deviceId()).map(z -> z.getId()).orElse(null);
        if (zoneId == null) return true;
        return securitySettingsRepository.findByFarmId(command.farmId())
                .map(settings -> settings.isZoneDetectionEnabled(zoneId))
                .orElse(true);
    }

    @Override
    public Result<SecurityEvent, ApplicationError> acknowledgeSecurityEvent(AcknowledgeSecurityEventCommand command) {
        var event = securityEventRepository.findById(command.eventId())
                .orElseThrow(() -> new SecurityEventNotFoundException(command.eventId()));
        event.acknowledge(command.acknowledgedBy());
        return Result.success(securityEventRepository.save(event));
    }
}
