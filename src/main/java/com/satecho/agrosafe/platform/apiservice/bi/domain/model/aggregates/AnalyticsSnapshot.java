package com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class AnalyticsSnapshot extends AuditableAbstractAggregateRoot<AnalyticsSnapshot> {

    @Setter private Long id;
    @Setter private SnapshotPeriod period;
    @Setter private Instant fromDate;
    @Setter private Instant toDate;
    @Setter private Integer activeUsers;
    @Setter private Double mrr;
    @Setter private String currency;
    @Setter private Double conversionRate;
    @Setter private Double churnRate;
    @Setter private Integer newSubscriptions;
    @Setter private Integer canceledSubscriptions;
    @Setter private String rawData;

    public AnalyticsSnapshot() {
    }

    public AnalyticsSnapshot(SnapshotPeriod period, Instant fromDate, Instant toDate,
                             Integer activeUsers, Double mrr, String currency,
                             Double conversionRate, Double churnRate,
                             Integer newSubscriptions, Integer canceledSubscriptions,
                             String rawData) {
        this.period = period;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.activeUsers = activeUsers;
        this.mrr = mrr;
        this.currency = currency;
        this.conversionRate = conversionRate;
        this.churnRate = churnRate;
        this.newSubscriptions = newSubscriptions;
        this.canceledSubscriptions = canceledSubscriptions;
        this.rawData = rawData;
    }
}
