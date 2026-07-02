package com.satecho.agrosafe.platform.apiservice.communication.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.DeviceToken;

import java.util.List;
import java.util.Optional;

public interface DeviceTokenRepository {
    DeviceToken save(DeviceToken token);
    List<DeviceToken> findByUserId(Long userId);
    Optional<DeviceToken> findByUserIdAndFcmToken(Long userId, String fcmToken);
    void deleteByFcmToken(String fcmToken);
}
