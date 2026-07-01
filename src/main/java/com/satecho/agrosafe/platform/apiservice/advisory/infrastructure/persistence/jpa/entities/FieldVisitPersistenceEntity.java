package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.VisitStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "field_visits")
@Getter
@Setter
@NoArgsConstructor
public class FieldVisitPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "agronomist_id", nullable = false)
    private Long agronomistId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;

    @Column(nullable = false, length = 100)
    private String tag;

    @Column(name = "note_title", nullable = false, length = 200)
    private String noteTitle;

    @Column(name = "note_body", length = 1000)
    private String noteBody;

    @Column(nullable = false)
    private Boolean urgent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VisitStatus status;
}
