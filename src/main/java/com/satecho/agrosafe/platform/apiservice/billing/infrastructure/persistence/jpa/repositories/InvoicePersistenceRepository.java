package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoicePersistenceRepository extends JpaRepository<InvoicePersistenceEntity, Long> {
    List<InvoicePersistenceEntity> findByUserIdOrderByIssuedAtDesc(Long userId);
}
