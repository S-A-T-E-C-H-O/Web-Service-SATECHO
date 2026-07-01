package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "analytics_snapshots")
@Getter
@Setter
@NoArgsConstructor
public class AnalyticsSnapshotPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SnapshotPeriod period;

    @Column(name = "from_date", nullable = false)
    private Instant fromDate;

    @Column(name = "to_date", nullable = false)
    private Instant toDate;

    @Column(name = "active_users")
    private Integer activeUsers;

    @Column
    private Double mrr;

    @Column(length = 10)
    private String currency;

    @Column(name = "conversion_rate")
    private Double conversionRate;

    @Column(name = "churn_rate")
    private Double churnRate;

    @Column(name = "new_subscriptions")
    private Integer newSubscriptions;

    @Column(name = "canceled_subscriptions")
    private Integer canceledSubscriptions;

    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;
}
