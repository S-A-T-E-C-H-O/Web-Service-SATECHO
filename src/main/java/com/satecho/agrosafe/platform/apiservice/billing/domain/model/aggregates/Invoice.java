package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.InvoiceStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * A billing record for a subscription change (EP-012-US003).
 * Since no actual payment processor integration is configured, invoices are recorded as
 * PAID immediately upon subscription change to track billing history.
 */
@Getter
public class Invoice extends AuditableAbstractAggregateRoot<Invoice> {
    /**
     * The unique identifier of the invoice.
     */
    @Setter private Long id;

    /**
     * The associated subscription ID.
     */
    @Setter private Long subscriptionId;

    /**
     * The user ID associated with this invoice.
     */
    @Setter private Long userId;

    /**
     * The billing amount of the invoice.
     */
    @Setter private Double amount;

    /**
     * The billing currency (e.g., USD).
     */
    @Setter private String currency;

    /**
     * The current payment/billing status of this invoice.
     */
    @Setter private InvoiceStatus status;

    /**
     * A description of what this invoice represents.
     */
    @Setter private String description;

    /**
     * The timestamp when this invoice was generated.
     */
    @Setter private Instant issuedAt;

    /**
     * The timestamp when this invoice was marked paid.
     */
    @Setter private Instant paidAt;

    /**
     * Empty constructor required by standard JPA/Framework usage.
     */
    public Invoice() {}

    /**
     * Constructs a new Invoice with default initialization.
     * Automatically sets currency to USD, status to PAID, and timestamps to the current instant.
     *
     * @param subscriptionId the ID of the associated subscription
     * @param userId the ID of the subscriber
     * @param amount the billing amount
     * @param description a textual description of the billing event
     */
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
