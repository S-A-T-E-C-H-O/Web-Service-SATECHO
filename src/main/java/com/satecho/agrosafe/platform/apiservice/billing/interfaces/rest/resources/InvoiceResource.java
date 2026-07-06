package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

import java.time.Instant;

/**
 * REST response resource representation of an Invoice.
 * Field names align with UI pages of the Mobile App.
 *
 * @param id the invoice identifier
 * @param amount the billing charge amount
 * @param currency the charge currency (e.g. USD)
 * @param status the payment transaction status
 * @param description a textual explanation of invoice line items
 * @param issuedAt the date and time when the invoice was issued
 */
public record InvoiceResource(Long id, Double amount, String currency, String status, String description,
                               Instant issuedAt) {
}
