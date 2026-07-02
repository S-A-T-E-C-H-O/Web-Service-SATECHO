package com.satecho.agrosafe.platform.apiservice.iot.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository {
    Optional<Device> findById(Long id);
    List<Device> findAll();
    List<Device> findByUserId(Long userId);
    List<Device> findByUserIdAndType(Long userId, DeviceType type);
    List<Device> findByUserIdAndStatus(Long userId, DeviceStatus status);
    List<Device> findByUserIdAndTypeAndStatus(Long userId, DeviceType type, DeviceStatus status);
    Optional<Device> findBySerialNumber(String serialNumber);
    Device save(Device device);
    boolean existsBySerialNumber(String serialNumber);
}
