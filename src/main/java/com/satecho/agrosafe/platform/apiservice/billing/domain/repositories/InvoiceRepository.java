package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;

import java.util.List;

/**
 * Repository interface for managing Invoice aggregates.
 */
public interface InvoiceRepository {
    /**
     * Persists or updates an Invoice in the database.
     *
     * @param invoice the invoice to save
     * @return the saved {@link Invoice} aggregate
     */
    Invoice save(Invoice invoice);

    /**
     * Finds all invoices for a user sorted by their issuance date in descending order.
     *
     * @param userId the user identifier
     * @return list of matching {@link Invoice} records
     */
    List<Invoice> findByUserIdOrderByIssuedAtDesc(Long userId);
}
