package com.satecho.agrosafe.platform.apiservice.communication.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.DeviceTokenCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.DeviceToken;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.RegisterDeviceTokenCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.DeviceTokenRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeviceTokenCommandServiceImpl implements DeviceTokenCommandService {

    private final DeviceTokenRepository deviceTokenRepository;

    public DeviceTokenCommandServiceImpl(DeviceTokenRepository deviceTokenRepository) {
        this.deviceTokenRepository = deviceTokenRepository;
    }

    @Override
    public Result<DeviceToken, ApplicationError> registerToken(RegisterDeviceTokenCommand command) {
        var existing = deviceTokenRepository.findByUserIdAndFcmToken(command.userId(), command.fcmToken());
        if (existing.isPresent()) {
            var token = existing.get();
            token.touch();
            return Result.success(deviceTokenRepository.save(token));
        }
        var token = new DeviceToken(command.userId(), command.fcmToken(), command.platform());
        return Result.success(deviceTokenRepository.save(token));
    }
}
