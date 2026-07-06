package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository client for the 'invoices' database table operations.
 */
@Repository
public interface InvoicePersistenceRepository extends JpaRepository<InvoicePersistenceEntity, Long> {
    
    /**
     * Queries database to find all invoice records for a user, sorted descending by their issued timestamp.
     *
     * @param userId the user identifier
     * @return list of matching invoice entities
     */
    List<InvoicePersistenceEntity> findByUserIdOrderByIssuedAtDesc(Long userId);
}
