package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.InvoiceStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity mapping representing database records in the 'billing_invoices' table.
 */
@Entity(name = "BillingInvoicePersistenceEntity")
@Table(name = "billing_invoices")
@Getter
@Setter
@NoArgsConstructor
public class InvoicePersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The ID of the subscription this invoice relates to.
     */
    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    /**
     * The ID of the user billed.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * The billing transaction amount.
     */
    @Column(name = "amount")
    private Double amount;

    /**
     * The 3-character ISO currency code.
     */
    @Column(name = "currency", length = 3)
    private String currency;

    /**
     * The payment outcome status of this invoice.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvoiceStatus status;

    /**
     * The description detailing the payment line items.
     */
    @Column(name = "description", length = 300)
    private String description;

    /**
     * The timestamp of invoice creation.
     */
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    /**
     * The timestamp of invoice settlement.
     */
    @Column(name = "paid_at")
    private Instant paidAt;
}
