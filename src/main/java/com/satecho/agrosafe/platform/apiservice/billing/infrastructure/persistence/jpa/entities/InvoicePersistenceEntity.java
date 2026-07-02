package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.InvoiceStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
public class InvoicePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "currency", length = 3)
    private String currency;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private InvoiceStatus status;
    @Column(name = "description", length = 300)
    private String description;
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;
    @Column(name = "paid_at")
    private Instant paidAt;
}
