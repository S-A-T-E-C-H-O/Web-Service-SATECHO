package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers.InvoicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories.InvoicePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private final InvoicePersistenceRepository invoicePersistenceRepository;

    public InvoiceRepositoryImpl(InvoicePersistenceRepository invoicePersistenceRepository) {
        this.invoicePersistenceRepository = invoicePersistenceRepository;
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return invoicePersistenceRepository.findById(id).map(InvoicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Invoice> findByUserId(Long userId) {
        return invoicePersistenceRepository.findByUserId(userId).stream()
                .map(InvoicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Invoice> findBySubscriptionId(Long subscriptionId) {
        return invoicePersistenceRepository.findBySubscriptionId(subscriptionId).stream()
                .map(InvoicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Invoice save(Invoice invoice) {
        var saved = invoicePersistenceRepository.save(InvoicePersistenceAssembler.toPersistenceFromDomain(invoice));
        return InvoicePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
