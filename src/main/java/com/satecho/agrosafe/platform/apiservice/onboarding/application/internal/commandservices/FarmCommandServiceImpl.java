package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.FarmCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.OnboardingCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.exceptions.FarmNotFoundException;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events.FarmCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FarmCommandServiceImpl implements FarmCommandService {

    private final FarmRepository farmRepository;
    private final OnboardingCommandService onboardingCommandService;

    public FarmCommandServiceImpl(FarmRepository farmRepository, OnboardingCommandService onboardingCommandService) {
        this.farmRepository = farmRepository;
        this.onboardingCommandService = onboardingCommandService;
    }

    @Override
    public Result<Farm, ApplicationError> createFarm(CreateFarmCommand command) {
        var farm = new Farm(command.userId(), command.name(), command.location(),
                command.hectares(), command.cropType());
        var savedFarm = farmRepository.save(farm);

        savedFarm.addDomainEvent(new FarmCreatedEvent(savedFarm,
                savedFarm.getId(), command.userId(), command.name(),
                command.cropType(), command.hectares(), command.location()));

        onboardingCommandService.ensureProgress(command.userId());
        return Result.success(savedFarm);
    }

    @Override
    public Result<Farm, ApplicationError> updateFarm(Long farmId, UpdateFarmCommand command) {
        var farm = farmRepository.findById(farmId)
                .orElseThrow(() -> new FarmNotFoundException(farmId));

        String name = command.name() != null ? command.name() : farm.getName();
        String location = command.location() != null ? command.location() : farm.getLocation();
        double hectares = command.hectares() > 0 ? command.hectares() : farm.getHectares();
        var cropType = command.cropType() != null ? command.cropType() : farm.getCropType();

        farm.update(name, location, hectares, cropType);
        return Result.success(farmRepository.save(farm));
    }

    @Override
    public Result<Void, ApplicationError> deleteFarm(Long farmId) {
        if (!farmRepository.existsById(farmId)) {
            return Result.failure(ApplicationError.notFound("Farm", String.valueOf(farmId)));
        }
        farmRepository.deleteById(farmId);
        return Result.success(null);
    }
}