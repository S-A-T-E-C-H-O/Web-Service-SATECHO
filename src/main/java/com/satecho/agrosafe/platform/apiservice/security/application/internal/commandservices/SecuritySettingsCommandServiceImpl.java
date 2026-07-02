package com.satecho.agrosafe.platform.apiservice.security.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecuritySettingsCommandService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.UpdateSecuritySettingsCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecuritySettingsRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SecuritySettingsCommandServiceImpl implements SecuritySettingsCommandService {

    private final SecuritySettingsRepository securitySettingsRepository;

    public SecuritySettingsCommandServiceImpl(SecuritySettingsRepository securitySettingsRepository) {
        this.securitySettingsRepository = securitySettingsRepository;
    }

    @Override
    public Result<SecuritySettings, ApplicationError> updateSettings(UpdateSecuritySettingsCommand command) {
        var settings = securitySettingsRepository.findByFarmId(command.farmId())
                .orElseGet(() -> securitySettingsRepository.save(new SecuritySettings(command.farmId())));
        settings.update(command.motionSensitivity(), command.alertMode(),
                command.detectionScheduleStart(), command.detectionScheduleEnd(), command.notificationContacts());
        return Result.success(securitySettingsRepository.save(settings));
    }

    @Override
    public Result<SecuritySettings, ApplicationError> setZoneDetectionEnabled(Long farmId, Long zoneId, boolean enabled) {
        var settings = securitySettingsRepository.findByFarmId(farmId)
                .orElseGet(() -> securitySettingsRepository.save(new SecuritySettings(farmId)));
        settings.setZoneDetectionEnabled(zoneId, enabled);
        return Result.success(securitySettingsRepository.save(settings));
    }
}
