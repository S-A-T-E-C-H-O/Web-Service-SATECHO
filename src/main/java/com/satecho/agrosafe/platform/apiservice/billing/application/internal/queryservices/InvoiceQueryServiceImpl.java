package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.InvoiceQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

<<<<<<< HEAD
@Service("billingInvoiceQueryServiceImpl")
=======
/**
 * Implementation of {@link InvoiceQueryService}.
 * Handles querying billing invoices for users.
 */
@Service
>>>>>>> release/1.9.0
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    /**
     * Repository used to retrieve invoice persistence data.
     */
    private final InvoiceRepository invoiceRepository;

    /**
     * Constructs a new InvoiceQueryServiceImpl.
     *
     * @param invoiceRepository the repository for managing invoices
     */
    public InvoiceQueryServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Retrieves all invoices associated with a user, sorted descending by their issue date.
     *
     * @param userId the ID of the user
     * @return list of {@link Invoice} entities
     */
    @Override
    public List<Invoice> findByUserId(Long userId) {
        return invoiceRepository.findByUserIdOrderByIssuedAtDesc(userId);
    }
}
