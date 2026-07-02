package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices.IrrigationCommandService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions.ActiveSessionExistsException;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.exceptions.NoActiveSessionException;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events.IrrigationStartedEvent;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events.IrrigationStoppedEvent;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.IrrigationSessionRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.mqtt.MqttActuatorPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IrrigationCommandServiceImpl implements IrrigationCommandService {

    private static final double DEFAULT_FLOW_RATE = 10.0;
    private final IrrigationSessionRepository sessionRepository;
    private final MqttActuatorPublisher mqttPublisher;
    private final ApplicationEventPublisher eventPublisher;

    public IrrigationCommandServiceImpl(IrrigationSessionRepository sessionRepository, MqttActuatorPublisher mqttPublisher,
                                         ApplicationEventPublisher eventPublisher) {
        this.sessionRepository = sessionRepository;
        this.mqttPublisher = mqttPublisher;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Result<IrrigationSession, ApplicationError> startIrrigation(StartIrrigationCommand command) {
        sessionRepository.findActiveByZoneId(command.zoneId()).ifPresent(s -> {
            throw new ActiveSessionExistsException(command.zoneId());
        });
        var session = new IrrigationSession(command.farmId(), command.zoneId(), command.deviceId(), command.triggeredBy(), command.durationMinutes());
        var saved = sessionRepository.save(session);
        mqttPublisher.publishOpen(command.farmId(), command.deviceId(), command.durationMinutes());
        eventPublisher.publishEvent(new IrrigationStartedEvent(saved.getId(), saved.getZoneId(), saved.getDeviceId(),
                saved.getTriggeredBy(), saved.getDurationMinutes(), saved.getStartedAt()));
        return Result.success(saved);
    }

    @Override
    public Result<IrrigationSession, ApplicationError> stopIrrigation(StopIrrigationCommand command) {
        var session = sessionRepository.findByZoneIdAndStatus(command.zoneId(), IrrigationStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveSessionException(command.zoneId()));
        double flowRate = command.flowRateLitersPerMinute() != null ? command.flowRateLitersPerMinute() : DEFAULT_FLOW_RATE;
        session.stop(flowRate);
        var saved = sessionRepository.save(session);
        mqttPublisher.publishClose(saved.getFarmId(), saved.getDeviceId());
        eventPublisher.publishEvent(new IrrigationStoppedEvent(saved.getId(), saved.getZoneId(),
                saved.getTotalWaterUsedLiters(), saved.getDurationMinutes(), saved.getStoppedAt()));
        return Result.success(saved);
    }
}
