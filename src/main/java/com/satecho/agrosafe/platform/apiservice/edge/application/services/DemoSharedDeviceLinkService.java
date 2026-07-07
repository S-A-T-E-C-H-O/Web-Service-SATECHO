package com.satecho.agrosafe.platform.apiservice.edge.application.services;

import com.satecho.agrosafe.platform.apiservice.edge.infrastructure.persistence.jpa.entities.DemoSharedDeviceLinkPersistenceEntity;
import com.satecho.agrosafe.platform.apiservice.edge.infrastructure.persistence.jpa.repositories.DemoSharedDeviceLinkPersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DemoSharedDeviceLinkService {

    private final DemoSharedDeviceLinkPersistenceRepository linkRepository;
    private final DeviceRepository deviceRepository;
    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final boolean enabled;
    private final String serialNumber;

    public DemoSharedDeviceLinkService(DemoSharedDeviceLinkPersistenceRepository linkRepository,
                                       DeviceRepository deviceRepository,
                                       FarmQueryService farmQueryService,
                                       ZoneQueryService zoneQueryService,
                                       @Value("${app.edge.demo-shared-device.enabled:false}") boolean enabled,
                                       @Value("${app.edge.demo-shared-device.serial-number:SEN-SAT-0401}") String serialNumber) {
        this.linkRepository = linkRepository;
        this.deviceRepository = deviceRepository;
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.enabled = enabled;
        this.serialNumber = serialNumber;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String serialNumber() {
        return serialNumber;
    }

    public Optional<Device> findPhysicalDevice() {
        if (!enabled || serialNumber == null || serialNumber.isBlank()) return Optional.empty();
        return deviceRepository.findBySerialNumber(serialNumber);
    }

    public boolean isPhysicalDemoDevice(Long deviceId) {
        return findPhysicalDevice().map(device -> device.getId().equals(deviceId)).orElse(false);
    }

    @Transactional
    public DemoSharedDeviceLinkPersistenceEntity claim(Long userId, Long farmId, Long zoneId) {
        if (!enabled) {
            throw new IllegalStateException("Demo shared device mode is disabled");
        }
        var farm = farmQueryService.findById(farmId)
                .orElseThrow(() -> new IllegalArgumentException("Farm not found"));
        if (!farm.getUserId().equals(userId)) {
            throw new SecurityException("The farm does not belong to the authenticated user");
        }
        var zone = zoneQueryService.findById(zoneId)
                .orElseThrow(() -> new IllegalArgumentException("Zone not found"));
        if (!zone.getFarmId().equals(farmId)) {
            throw new IllegalArgumentException("The zone does not belong to the selected farm");
        }
        var physicalDevice = findPhysicalDevice()
                .orElseThrow(() -> new IllegalStateException("Demo physical device was not found"));

        var link = linkRepository.findByUserIdAndFarmIdAndZoneIdAndActiveTrue(userId, farmId, zoneId)
                .orElseGet(DemoSharedDeviceLinkPersistenceEntity::new);
        link.setUserId(userId);
        link.setFarmId(farmId);
        link.setZoneId(zoneId);
        link.setPhysicalDeviceId(physicalDevice.getId());
        link.setSerialNumber(physicalDevice.getSerialNumber());
        link.setActive(true);
        return linkRepository.save(link);
    }

    public List<DemoSharedDeviceLinkPersistenceEntity> findActiveByUserId(Long userId) {
        if (!enabled) return List.of();
        return linkRepository.findByUserIdAndActiveTrue(userId);
    }

    public Optional<DemoSharedDeviceLinkPersistenceEntity> findActiveByZoneId(Long zoneId) {
        if (!enabled) return Optional.empty();
        return linkRepository.findFirstByZoneIdAndActiveTrue(zoneId);
    }

    public Optional<DemoSharedDeviceLinkPersistenceEntity> findActiveByFarmId(Long farmId) {
        if (!enabled) return Optional.empty();
        return linkRepository.findFirstByFarmIdAndActiveTrue(farmId);
    }

    public boolean hasActiveLink(Long physicalDeviceId, Long farmId, Long zoneId) {
        if (!enabled || physicalDeviceId == null || farmId == null || zoneId == null) return false;
        return linkRepository.existsByPhysicalDeviceIdAndFarmIdAndZoneIdAndActiveTrue(physicalDeviceId, farmId, zoneId);
    }

    public boolean hasActiveLinkForZone(Long physicalDeviceId, Long zoneId) {
        if (!enabled || physicalDeviceId == null || zoneId == null) return false;
        return linkRepository.existsByPhysicalDeviceIdAndZoneIdAndActiveTrue(physicalDeviceId, zoneId);
    }

    public SensorReading mapReadingToLinkedZone(SensorReading source, Long linkedZoneId) {
        var mapped = new SensorReading();
        mapped.setId(source.getId());
        mapped.setDeviceId(source.getDeviceId());
        mapped.setZoneId(linkedZoneId);
        mapped.setReadingValue(source.getReadingValue());
        mapped.setTimestamp(source.getTimestamp());
        mapped.setIngestedAt(source.getIngestedAt());
        mapped.setIsValid(source.getIsValid());
        return mapped;
    }
}
