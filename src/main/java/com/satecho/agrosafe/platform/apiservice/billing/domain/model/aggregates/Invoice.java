package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.InvoiceStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * A billing record for a subscription change (EP-012-US003). There's no real payment
 * gateway wired up (no processor credentials exist yet), so invoices are recorded as
 * PAID immediately at subscription time — this tracks billing history honestly without
 * pretending to be a production payment integration.
 */
@Getter
public class Invoice extends AuditableAbstractAggregateRoot<Invoice> {
    @Setter private Long id;
    @Setter private Long subscriptionId;
    @Setter private Long userId;
    @Setter private Double amount;
    @Setter private String currency;
    @Setter private InvoiceStatus status;
    @Setter private String description;
    @Setter private Instant issuedAt;
    @Setter private Instant paidAt;

    public Invoice() {}

    public Invoice(Long subscriptionId, Long userId, Double amount, String description) {
        this.subscriptionId = subscriptionId;
        this.userId = userId;
        this.amount = amount;
        this.currency = "USD";
        this.description = description;
        this.issuedAt = Instant.now();
        this.status = InvoiceStatus.PAID;
        this.paidAt = Instant.now();
    }
}
