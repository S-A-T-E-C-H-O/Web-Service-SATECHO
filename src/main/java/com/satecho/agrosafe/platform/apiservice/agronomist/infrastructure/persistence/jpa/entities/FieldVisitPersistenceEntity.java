package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @Column(name = "agronomist_user_id", nullable = false)
    private Long agronomistUserId;
    @Column(name = "farm_id", nullable = false)
    private Long farmId;
    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;
    @Column(name = "tag", length = 50)
    private String tag;
    @Column(name = "note_title", length = 200)
    private String noteTitle;
    @Column(name = "note_body", columnDefinition = "TEXT")
    private String noteBody;
    @Column(name = "urgent", nullable = false)
    private Boolean urgent;
    @Column(name = "completed", nullable = false)
    private Boolean completed;
    @Column(name = "completed_at")
    private Instant completedAt;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;
}
