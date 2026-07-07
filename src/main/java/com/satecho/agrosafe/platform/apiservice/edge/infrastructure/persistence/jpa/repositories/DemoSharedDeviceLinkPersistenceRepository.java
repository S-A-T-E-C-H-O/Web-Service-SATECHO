package com.satecho.agrosafe.platform.apiservice.edge.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.edge.infrastructure.persistence.jpa.entities.DemoSharedDeviceLinkPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DemoSharedDeviceLinkPersistenceRepository extends JpaRepository<DemoSharedDeviceLinkPersistenceEntity, Long> {
    List<DemoSharedDeviceLinkPersistenceEntity> findByUserIdAndActiveTrue(Long userId);
    Optional<DemoSharedDeviceLinkPersistenceEntity> findByUserIdAndFarmIdAndZoneIdAndActiveTrue(Long userId, Long farmId, Long zoneId);
    Optional<DemoSharedDeviceLinkPersistenceEntity> findFirstByZoneIdAndActiveTrue(Long zoneId);
    Optional<DemoSharedDeviceLinkPersistenceEntity> findFirstByFarmIdAndActiveTrue(Long farmId);
    boolean existsByPhysicalDeviceIdAndFarmIdAndZoneIdAndActiveTrue(Long physicalDeviceId, Long farmId, Long zoneId);
    boolean existsByPhysicalDeviceIdAndZoneIdAndActiveTrue(Long physicalDeviceId, Long zoneId);
}
