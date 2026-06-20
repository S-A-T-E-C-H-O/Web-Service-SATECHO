package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities.SecuritySettingsPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecuritySettingsPersistenceRepository extends JpaRepository<SecuritySettingsPersistenceEntity, Long> {
    Optional<SecuritySettingsPersistenceEntity> findByFarmId(Long farmId);
}
