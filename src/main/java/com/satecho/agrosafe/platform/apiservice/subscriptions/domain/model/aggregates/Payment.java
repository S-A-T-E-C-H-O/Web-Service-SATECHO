package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private Long subscriptionId;
    @Setter private Long invoiceId;
    @Setter private Double amount;
    @Setter private String currency;
    @Setter private PaymentMethod paymentMethod;
    @Setter private String status;
    @Setter private String transactionId;
    @Setter private Instant processedAt;

    public Payment() {
    }

    public Payment(Long userId, Long subscriptionId, Long invoiceId, Double amount,
                   String currency, PaymentMethod paymentMethod, String transactionId) {
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.status = "COMPLETED";
        this.transactionId = transactionId;
        this.processedAt = Instant.now();
    }
}
