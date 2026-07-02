package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.InvoicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.InvoicePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoicePersistenceRepository persistenceRepository;

    public InvoiceRepositoryImpl(InvoicePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Invoice save(Invoice invoice) {
        var saved = persistenceRepository.save(InvoicePersistenceAssembler.toPersistenceFromDomain(invoice));
        return InvoicePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<Invoice> findByUserIdOrderByIssuedAtDesc(Long userId) {
        return persistenceRepository.findByUserIdOrderByIssuedAtDesc(userId).stream()
                .map(InvoicePersistenceAssembler::toDomainFromPersistence).toList();
    }
}
