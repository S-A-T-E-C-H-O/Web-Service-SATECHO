package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.DeviceToken;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.DeviceTokenRepository;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers.DeviceTokenPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories.DeviceTokenPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceTokenRepositoryImpl implements DeviceTokenRepository {

    private final DeviceTokenPersistenceRepository persistenceRepository;

    public DeviceTokenRepositoryImpl(DeviceTokenPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public DeviceToken save(DeviceToken token) {
        var saved = persistenceRepository.save(DeviceTokenPersistenceAssembler.toPersistenceFromDomain(token));
        return DeviceTokenPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<DeviceToken> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).stream()
                .map(DeviceTokenPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<DeviceToken> findByUserIdAndFcmToken(Long userId, String fcmToken) {
        return persistenceRepository.findByUserIdAndFcmToken(userId, fcmToken)
                .map(DeviceTokenPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public void deleteByFcmToken(String fcmToken) {
        persistenceRepository.deleteByFcmToken(fcmToken);
    }
}
