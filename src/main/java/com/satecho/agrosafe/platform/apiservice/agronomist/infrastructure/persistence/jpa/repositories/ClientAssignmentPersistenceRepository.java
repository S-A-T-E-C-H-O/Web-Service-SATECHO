package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.ClientAssignmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAssignmentPersistenceRepository extends JpaRepository<ClientAssignmentPersistenceEntity, Long> {
    List<ClientAssignmentPersistenceEntity> findByAgronomistUserId(Long agronomistUserId);
    Optional<ClientAssignmentPersistenceEntity> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
    Optional<ClientAssignmentPersistenceEntity> findByFarmerUserId(Long farmerUserId);
    boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
}
