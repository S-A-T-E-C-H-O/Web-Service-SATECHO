package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * An agronomist's scheduled or completed field visit (EP-009-US008). Mirrors what the
 * Mobile App's agenda screen already sends/expects: a farm-scoped task with a note and
 * an urgency flag, completed explicitly rather than logged only after the fact.
 */
@Getter
public class FieldVisit extends AuditableAbstractAggregateRoot<FieldVisit> {
    @Setter private Long id;
    @Setter private Long agronomistUserId;
    @Setter private Long farmId;
    @Setter private Instant scheduledAt;
    @Setter private String tag;
    @Setter private String noteTitle;
    @Setter private String noteBody;
    @Setter private Boolean urgent;
    @Setter private Boolean completed;
    @Setter private Instant completedAt;
    @Setter private Double latitude;
    @Setter private Double longitude;
    @Setter private String photoBase64;

    public FieldVisit() {}

    public FieldVisit(Long agronomistUserId, Long farmId, Instant scheduledAt, String tag,
                       String noteTitle, String noteBody, Boolean urgent) {
        if (agronomistUserId == null) throw new IllegalArgumentException("agronomistUserId cannot be null");
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        this.agronomistUserId = agronomistUserId;
        this.farmId = farmId;
        this.scheduledAt = scheduledAt != null ? scheduledAt : Instant.now();
        this.tag = tag;
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.urgent = urgent != null && urgent;
        this.completed = false;
    }

    public void complete() {
        this.completed = true;
        this.completedAt = Instant.now();
    }

    public void recordLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** JPEG captured on-device, base64-encoded — no object storage configured for this project yet. */
    public void attachPhoto(String photoBase64) {
        this.photoBase64 = photoBase64;
    }
}
