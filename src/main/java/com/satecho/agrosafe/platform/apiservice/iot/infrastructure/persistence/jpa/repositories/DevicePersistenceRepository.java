package com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevicePersistenceRepository extends JpaRepository<DevicePersistenceEntity, Long> {
    List<DevicePersistenceEntity> findByUserId(Long userId);
    List<DevicePersistenceEntity> findByUserIdAndType(Long userId, DeviceType type);
    List<DevicePersistenceEntity> findByUserIdAndStatus(Long userId, DeviceStatus status);
    List<DevicePersistenceEntity> findByUserIdAndTypeAndStatus(Long userId, DeviceType type, DeviceStatus status);
    Optional<DevicePersistenceEntity> findBySerialNumber(String serialNumber);
    boolean existsBySerialNumber(String serialNumber);
}
