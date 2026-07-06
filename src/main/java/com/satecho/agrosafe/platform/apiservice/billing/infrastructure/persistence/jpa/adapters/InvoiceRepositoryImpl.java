package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.InvoicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.InvoicePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA implementation adapter for the {@link InvoiceRepository} port.
 * Handles domain-persistence model transformation and database interactions.
 */
@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    /**
     * Spring Data JPA database client repository.
     */
    private final InvoicePersistenceRepository persistenceRepository;

    /**
     * Constructs a new InvoiceRepositoryImpl.
     *
     * @param persistenceRepository the underlying persistence repository
     */
    public InvoiceRepositoryImpl(InvoicePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Transforms the domain Invoice into a persistence entity, saves it, and maps it back.
     *
     * @param invoice the invoice domain model
     * @return the persisted invoice domain model
     */
    @Override
    public Invoice save(Invoice invoice) {
        var saved = persistenceRepository.save(InvoicePersistenceAssembler.toPersistenceFromDomain(invoice));
        return InvoicePersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Queries invoice entities matching the userId, sorts by issued date descending, and returns domain models.
     *
     * @param userId the user identifier
     * @return list of invoice domain models
     */
    @Override
    public List<Invoice> findByUserIdOrderByIssuedAtDesc(Long userId) {
        return persistenceRepository.findByUserIdOrderByIssuedAtDesc(userId).stream()
                .map(InvoicePersistenceAssembler::toDomainFromPersistence).toList();
    }
}
