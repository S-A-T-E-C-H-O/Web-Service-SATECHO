package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.SuspendedAccountPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuspendedAccountPersistenceRepository extends JpaRepository<SuspendedAccountPersistenceEntity, Long> {

    @Query("SELECT sa FROM SuspendedAccountPersistenceEntity sa WHERE sa.userId = :userId AND sa.reactivatedAt IS NULL " +
           "ORDER BY sa.suspendedAt DESC")
    Optional<SuspendedAccountPersistenceEntity> findActiveSuspensionByUserId(@Param("userId") Long userId);
}
