package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Invoice extends AuditableAbstractAggregateRoot<Invoice> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private Long subscriptionId;
    @Setter private Double amount;
    @Setter private String currency;
    @Setter private String description;
    @Setter private InvoiceStatus status;
    @Setter private Instant billingPeriodStart;
    @Setter private Instant billingPeriodEnd;
    @Setter private Instant dueDate;
    @Setter private Instant paidAt;
    @Setter private String paymentMethod;
    @Setter private String transactionId;

    public Invoice() {
    }

    public Invoice(Long userId, Long subscriptionId, Double amount, String description,
                   Instant billingPeriodStart, Instant billingPeriodEnd, Instant dueDate) {
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.currency = "PEN";
        this.description = description;
        this.status = InvoiceStatus.PENDING;
        this.billingPeriodStart = billingPeriodStart;
        this.billingPeriodEnd = billingPeriodEnd;
        this.dueDate = dueDate;
    }

    public void markAsPaid(String paymentMethod, String transactionId) {
        this.status = InvoiceStatus.PAID;
        this.paidAt = Instant.now();
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
    }

    public void markAsOverdue() {
        this.status = InvoiceStatus.OVERDUE;
    }

    public void markAsCanceled() {
        this.status = InvoiceStatus.CANCELED;
    }
}
