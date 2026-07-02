package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories;

import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.entities.DeviceTokenPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenPersistenceRepository extends JpaRepository<DeviceTokenPersistenceEntity, Long> {
    List<DeviceTokenPersistenceEntity> findByUserId(Long userId);
    Optional<DeviceTokenPersistenceEntity> findByUserIdAndFcmToken(Long userId, String fcmToken);
    void deleteByFcmToken(String fcmToken);
}
