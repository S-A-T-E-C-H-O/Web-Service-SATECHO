package com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class DiagnosticSession extends AuditableAbstractAggregateRoot<DiagnosticSession> {

    @Setter private Long id;
    @Setter private Long deviceId;
    @Setter private String status;
    @Setter private String componentResults;
    @Setter private String recommendation;
    @Setter private Instant startedAt;
    @Setter private Instant completedAt;

    public DiagnosticSession() {
    }

    public DiagnosticSession(Long deviceId) {
        this.deviceId = deviceId;
        this.status = "PENDING";
        this.startedAt = Instant.now();
    }

    public void complete(String componentResults, String recommendation) {
        this.status = "COMPLETED";
        this.componentResults = componentResults;
        this.recommendation = recommendation;
        this.completedAt = Instant.now();
    }

    public void fail(String reason) {
        this.status = "FAILED";
        this.componentResults = reason;
        this.completedAt = Instant.now();
    }
}
