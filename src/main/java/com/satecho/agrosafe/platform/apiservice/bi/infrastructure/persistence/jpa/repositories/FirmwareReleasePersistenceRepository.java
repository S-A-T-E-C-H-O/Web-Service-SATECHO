package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.FirmwareReleasePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FirmwareReleasePersistenceRepository extends JpaRepository<FirmwareReleasePersistenceEntity, Long> {

    List<FirmwareReleasePersistenceEntity> findByDeviceModelOrderByReleasedAtDesc(String deviceModel);

    Optional<FirmwareReleasePersistenceEntity> findByDeviceModelAndActiveTrue(String deviceModel);
}
