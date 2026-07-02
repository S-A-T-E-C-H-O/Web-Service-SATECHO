package com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.InvoiceQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<Invoice> findByUserId(Long userId) {
        return invoiceRepository.findByUserIdOrderByIssuedAtDesc(userId);
    }
}
