package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

import java.time.Instant;

/** Field names match Mobile App's InvoicesPage tile reads (amount/currency/status/description). */
public record InvoiceResource(Long id, Double amount, String currency, String status, String description,
                               Instant issuedAt) {
}
