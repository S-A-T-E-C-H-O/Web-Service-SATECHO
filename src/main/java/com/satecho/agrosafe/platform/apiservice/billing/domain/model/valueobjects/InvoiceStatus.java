package com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects;

/**
 * Represents the payment status of an invoice.
 */
public enum InvoiceStatus {
    /**
     * Indicates that the invoice has been successfully paid.
     */
    PAID,

    /**
     * Indicates that the invoice payment is pending completion.
     */
    PENDING,

    /**
     * Indicates that the invoice payment attempt failed.
     */
    FAILED
}
