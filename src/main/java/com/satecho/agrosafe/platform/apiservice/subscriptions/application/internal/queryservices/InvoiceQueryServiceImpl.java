package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.InvoiceQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("subscriptionsInvoiceQueryServiceImpl")
@Transactional(readOnly = true)
public class InvoiceQueryServiceImpl implements InvoiceQueryService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceQueryServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public List<Invoice> findByUserId(Long userId) {
        return invoiceRepository.findByUserId(userId);
    }

    @Override
    public Optional<Invoice> findById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }
}
