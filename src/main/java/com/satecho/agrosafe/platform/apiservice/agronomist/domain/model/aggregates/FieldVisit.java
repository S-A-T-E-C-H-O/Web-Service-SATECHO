package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * An agronomist's scheduled or completed field visit (EP-009-US008). Mirrors what the
 * Mobile App's agenda screen already sends/expects: a farm-scoped task with a note and
 * an urgency flag, completed explicitly rather than logged only after the fact.
 *
 * @author Colegio
 * @version 1.0
 */
@Getter
public class FieldVisit extends AuditableAbstractAggregateRoot<FieldVisit> {
    /**
     * The unique identifier of the field visit.
     */
    @Setter private Long id;

    /**
     * The identifier of the agronomist user.
     */
    @Setter private Long agronomistUserId;

    /**
     * The identifier of the farm.
     */
    @Setter private Long farmId;

    /**
     * The scheduled timestamp of the field visit.
     */
    @Setter private Instant scheduledAt;

    /**
     * The tag or category of the field visit.
     */
    @Setter private String tag;

    /**
     * The title of the visit note.
     */
    @Setter private String noteTitle;

    /**
     * The body/content of the visit note.
     */
    @Setter private String noteBody;

    /**
     * Flag indicating if the visit is urgent.
     */
    @Setter private Boolean urgent;

    /**
     * Flag indicating if the visit is completed.
     */
    @Setter private Boolean completed;

    /**
     * The timestamp when the visit was completed.
     */
    @Setter private Instant completedAt;

    /**
     * The latitude of the visit location.
     */
    @Setter private Double latitude;

    /**
     * The longitude of the visit location.
     */
    @Setter private Double longitude;

    /**
     * Base64 encoded string of the captured photo.
     */
    @Setter private String photoBase64;

    /**
     * Default constructor for FieldVisit.
     */
    public FieldVisit() {}

    /**
     * Parameterized constructor for FieldVisit.
     *
     * @param agronomistUserId the ID of the agronomist user, must not be null
     * @param farmId           the ID of the farm, must not be null
     * @param scheduledAt      the scheduled time; if null, defaults to current time
     * @param tag              the tag or category of the visit
     * @param noteTitle        the title of the visit note
     * @param noteBody         the body of the visit note
     * @param urgent           flag indicating if the visit is urgent
     * @throws IllegalArgumentException if agronomistUserId or farmId is null
     */
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

    /**
     * Marks the field visit as completed and records the completion timestamp.
     */
    public void complete() {
        this.completed = true;
        this.completedAt = Instant.now();
    }

    /**
     * Records the geographic coordinates of the field visit.
     *
     * @param latitude  the latitude coordinate
     * @param longitude the longitude coordinate
     */
    public void recordLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Attaches a base64 encoded photo to the field visit.
     * JPEG captured on-device, base64-encoded — no object storage configured for this project yet.
     *
     * @param photoBase64 the base64 encoded photo string
     */
    public void attachPhoto(String photoBase64) {
        this.photoBase64 = photoBase64;
    }
}
