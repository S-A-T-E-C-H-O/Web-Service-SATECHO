package com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.GenerateInvoiceCommand;

public interface InvoiceCommandService {
    Result<Invoice, ApplicationError> generateInvoice(GenerateInvoiceCommand command);
}
