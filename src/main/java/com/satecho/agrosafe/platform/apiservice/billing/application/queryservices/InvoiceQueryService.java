package com.satecho.agrosafe.platform.apiservice.billing.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;

import java.util.List;

public interface InvoiceQueryService {
    List<Invoice> findByUserId(Long userId);
}
