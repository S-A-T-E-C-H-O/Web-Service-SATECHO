package com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DevicePersistenceRepository persistenceRepository;

    public DeviceRepositoryImpl(DevicePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<Device> findById(Long id) {
        return persistenceRepository.findById(id).map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Device> findAll() {
        return persistenceRepository.findAll().stream().map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Device> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).stream().map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Device> findByUserIdAndType(Long userId, DeviceType type) {
        return persistenceRepository.findByUserIdAndType(userId, type).stream().map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Device> findByUserIdAndStatus(Long userId, DeviceStatus status) {
        return persistenceRepository.findByUserIdAndStatus(userId, status).stream().map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Device> findByUserIdAndTypeAndStatus(Long userId, DeviceType type, DeviceStatus status) {
        return persistenceRepository.findByUserIdAndTypeAndStatus(userId, type, status).stream().map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<Device> findBySerialNumber(String serialNumber) {
        return persistenceRepository.findBySerialNumber(serialNumber).map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Device save(Device device) {
        var saved = persistenceRepository.save(DevicePersistenceAssembler.toPersistenceFromDomain(device));
        return DevicePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsBySerialNumber(String serialNumber) {
        return persistenceRepository.existsBySerialNumber(serialNumber);
    }
}
