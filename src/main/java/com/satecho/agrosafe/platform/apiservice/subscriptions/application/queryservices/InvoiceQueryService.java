package com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceQueryService {
    List<Invoice> findByUserId(Long userId);
    Optional<Invoice> findById(Long invoiceId);
}
