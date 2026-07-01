package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.InvoiceCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.GenerateInvoiceCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InvoiceCommandServiceImpl implements InvoiceCommandService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceCommandServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Result<Invoice, ApplicationError> generateInvoice(GenerateInvoiceCommand command) {
        Invoice invoice = new Invoice(
                command.userId(), command.subscriptionId(), command.amount(),
                command.description(), command.billingPeriodStart(),
                command.billingPeriodEnd(), command.dueDate());
        return Result.success(invoiceRepository.save(invoice));
    }
}
