package com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.CreateZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.LinkDeviceToZoneCommand;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.UpdateZoneThresholdCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface ZoneCommandService {
    Result<IrrigationZone, ApplicationError> createZone(CreateZoneCommand command);
    Result<IrrigationZone, ApplicationError> updateZone(Long zoneId, String name, double areaHectares);
    Result<IrrigationZone, ApplicationError> updateThresholds(UpdateZoneThresholdCommand command);
    Result<IrrigationZone, ApplicationError> linkDevice(LinkDeviceToZoneCommand command);
}