package com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateFarmCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface FarmCommandService {
    Result<Farm, ApplicationError> createFarm(CreateFarmCommand command);
    Result<Farm, ApplicationError> updateFarm(Long farmId, UpdateFarmCommand command);
    Result<Void, ApplicationError> deleteFarm(Long farmId);
    Result<Farm, ApplicationError> deactivateFarm(Long farmId);
    Result<Farm, ApplicationError> reactivateFarm(Long farmId);
}