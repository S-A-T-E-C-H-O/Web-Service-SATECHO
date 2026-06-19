package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.OnboardingCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.OnboardingProgress;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CompleteOnboardingCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events.OnboardingCompletedEvent;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.OnboardingProgressRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OnboardingCommandServiceImpl implements OnboardingCommandService {

    private final OnboardingProgressRepository onboardingProgressRepository;
    private final FarmRepository farmRepository;
    private final ZoneRepository zoneRepository;

    public OnboardingCommandServiceImpl(OnboardingProgressRepository onboardingProgressRepository,
                                        FarmRepository farmRepository, ZoneRepository zoneRepository) {
        this.onboardingProgressRepository = onboardingProgressRepository;
        this.farmRepository = farmRepository;
        this.zoneRepository = zoneRepository;
    }

    @Override
    public OnboardingProgress ensureProgress(Long userId) {
        var progress = onboardingProgressRepository.findByUserId(userId)
                .orElseGet(() -> onboardingProgressRepository.save(new OnboardingProgress(userId)));

        boolean hasFarms = !farmRepository.findAllByUserId(userId).isEmpty();
        boolean hasZones = false;
        boolean hasDeviceLinked = false;
        boolean hasThresholdsConfigured = false;

        if (hasFarms) {
            var farms = farmRepository.findAllByUserId(userId);
            for (var farm : farms) {
                var zones = zoneRepository.findAllByFarmId(farm.getId());
                if (!zones.isEmpty()) {
                    hasZones = true;
                    for (var zone : zones) {
                        if (zone.getDeviceId() != null) hasDeviceLinked = true;
                        if (zone.getThresholds() != null) hasThresholdsConfigured = true;
                    }
                }
            }
        }

        int step = 1;
        if (hasFarms) step = 2;
        if (hasZones) step = 3;
        if (hasDeviceLinked) step = 4;
        if (hasThresholdsConfigured) step = 5;

        progress.advanceStep(step, hasFarms, hasZones, hasDeviceLinked, hasThresholdsConfigured);
        return onboardingProgressRepository.save(progress);
    }

    @Override
    public Result<OnboardingProgress, ApplicationError> completeOnboarding(CompleteOnboardingCommand command) {
        var progress = onboardingProgressRepository.findByUserId(command.userId())
                .orElseGet(() -> onboardingProgressRepository.save(new OnboardingProgress(command.userId())));

        int farmCount = farmRepository.findAllByUserId(command.userId()).size();
        int zoneCount = (int) farmRepository.findAllByUserId(command.userId()).stream()
                .flatMap(farm -> zoneRepository.findAllByFarmId(farm.getId()).stream())
                .count();

        progress.complete();
        var savedProgress = onboardingProgressRepository.save(progress);

        savedProgress.addDomainEvent(new OnboardingCompletedEvent(savedProgress,
                command.userId(), savedProgress.getId(), farmCount, zoneCount));

        return Result.success(savedProgress);
    }
}