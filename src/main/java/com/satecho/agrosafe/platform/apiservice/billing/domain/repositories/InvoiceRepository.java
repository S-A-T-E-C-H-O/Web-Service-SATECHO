package com.satecho.agrosafe.platform.apiservice.billing.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;

import java.util.List;

public interface InvoiceRepository {
    Invoice save(Invoice invoice);
    List<Invoice> findByUserIdOrderByIssuedAtDesc(Long userId);
}
