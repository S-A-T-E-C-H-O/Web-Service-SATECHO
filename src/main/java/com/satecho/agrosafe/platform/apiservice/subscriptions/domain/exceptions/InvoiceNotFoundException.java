package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class InvoiceNotFoundException extends AgroSafeException {

    public InvoiceNotFoundException(Long invoiceId) {
        super("Invoice not found with ID: " + invoiceId);
    }
}
