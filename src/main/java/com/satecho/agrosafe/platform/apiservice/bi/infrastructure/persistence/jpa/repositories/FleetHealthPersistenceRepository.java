package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.FleetHealthPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FleetHealthPersistenceRepository extends JpaRepository<FleetHealthPersistenceEntity, Long> {

    Optional<FleetHealthPersistenceEntity> findTopByOrderBySnapshotAtDesc();
}
