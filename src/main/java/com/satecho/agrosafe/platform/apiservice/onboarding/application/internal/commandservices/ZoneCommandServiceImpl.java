package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.OnboardingCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.ZoneCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.exceptions.ZoneNotFoundException;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.LinkDeviceToZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateZoneThresholdCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ZoneCommandServiceImpl implements ZoneCommandService {

    private final ZoneRepository zoneRepository;
    private final FarmRepository farmRepository;
    private final OnboardingCommandService onboardingCommandService;

    public ZoneCommandServiceImpl(ZoneRepository zoneRepository, FarmRepository farmRepository,
                                  OnboardingCommandService onboardingCommandService) {
        this.zoneRepository = zoneRepository;
        this.farmRepository = farmRepository;
        this.onboardingCommandService = onboardingCommandService;
    }

    @Override
    public Result<IrrigationZone, ApplicationError> createZone(CreateZoneCommand command) {
        if (!farmRepository.existsById(command.farmId())) {
            return Result.failure(ApplicationError.notFound("Farm", String.valueOf(command.farmId())));
        }

        var zone = new IrrigationZone(command.farmId(), command.name(),
                command.areaHectares(), command.cropType());
        var savedZone = zoneRepository.save(zone);

        farmRepository.findById(command.farmId()).ifPresent(farm ->
                onboardingCommandService.ensureProgress(farm.getUserId()));

        return Result.success(savedZone);
    }

    @Override
    public Result<IrrigationZone, ApplicationError> updateZone(Long zoneId, String name, double areaHectares) {
        var zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ZoneNotFoundException(zoneId));

        if (name != null && !name.isBlank()) {
            zone.update(name, areaHectares > 0 ? areaHectares : zone.getAreaHectares(), zone.getCropType());
        } else if (areaHectares > 0) {
            zone.update(zone.getName(), areaHectares, zone.getCropType());
        }

        return Result.success(zoneRepository.save(zone));
    }

    @Override
    public Result<IrrigationZone, ApplicationError> updateThresholds(UpdateZoneThresholdCommand command) {
        var zone = zoneRepository.findById(command.zoneId())
                .orElseThrow(() -> new ZoneNotFoundException(command.zoneId()));

        zone.updateThresholds(command.thresholds());
        var savedZone = zoneRepository.save(zone);

        farmRepository.findById(savedZone.getFarmId()).ifPresent(farm ->
                onboardingCommandService.ensureProgress(farm.getUserId()));

        return Result.success(savedZone);
    }

    @Override
    public Result<IrrigationZone, ApplicationError> linkDevice(LinkDeviceToZoneCommand command) {
        var zone = zoneRepository.findById(command.zoneId())
                .orElseThrow(() -> new ZoneNotFoundException(command.zoneId()));

        zone.linkDevice(command.deviceId());
        var savedZone = zoneRepository.save(zone);

        farmRepository.findById(savedZone.getFarmId()).ifPresent(farm ->
                onboardingCommandService.ensureProgress(farm.getUserId()));

        return Result.success(savedZone);
    }
}