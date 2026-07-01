package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {
    Optional<Invoice> findById(Long id);
    List<Invoice> findByUserId(Long userId);
    List<Invoice> findBySubscriptionId(Long subscriptionId);
    Invoice save(Invoice invoice);
}
