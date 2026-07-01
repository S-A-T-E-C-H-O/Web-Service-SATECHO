package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.VisitStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class FieldVisit extends AuditableAbstractAggregateRoot<FieldVisit> {
    @Setter private Long id;
    @Setter private Long agronomistId;
    @Setter private Long farmId;
    @Setter private Instant scheduledAt;
    @Setter private String tag;
    @Setter private String noteTitle;
    @Setter private String noteBody;
    @Setter private Boolean urgent;
    @Setter private VisitStatus status;

    public FieldVisit() {}

    public FieldVisit(Long agronomistId, Long farmId, Instant scheduledAt,
                      String tag, String noteTitle, String noteBody, Boolean urgent) {
        this.agronomistId = agronomistId;
        this.farmId = farmId;
        this.scheduledAt = scheduledAt;
        this.tag = tag;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.urgent = urgent;
        this.status = VisitStatus.SCHEDULED;
    }

    public void complete() { this.status = VisitStatus.COMPLETED; }
    public void cancel()   { this.status = VisitStatus.CANCELLED; }
}
