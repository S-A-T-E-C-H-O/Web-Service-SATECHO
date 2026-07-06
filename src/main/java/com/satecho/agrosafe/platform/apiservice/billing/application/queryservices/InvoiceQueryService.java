package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;

import java.util.List;

/**
 * Query service interface for retrieving billing invoices.
 */
public interface InvoiceQueryService {
    /**
     * Finds all invoices associated with a user.
     *
     * @param userId the ID of the user
     * @return a list of {@link Invoice} aggregates
     */
    List<Invoice> findByUserId(Long userId);
}
