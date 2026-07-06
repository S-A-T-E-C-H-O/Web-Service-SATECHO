package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Persistence entity representing a field visit in the database.
 * <p>
 * This class maps the {@code field_visits} table and stores details about farm inspections
 * conducted by agronomists.
 * </p>
 */
@Entity
@Table(name = "field_visits")
@Getter
@Setter
@NoArgsConstructor
public class FieldVisitPersistenceEntity extends AuditableAbstractPersistenceEntity {
    
    /**
     * The ID of the agronomist user conducting the field visit.
     */
    @Column(name = "agronomist_user_id", nullable = false)
    private Long agronomistUserId;
    
    /**
     * The ID of the farm being visited.
     */
    @Column(name = "farm_id", nullable = false)
    private Long farmId;
    
    /**
     * The scheduled timestamp for the field visit.
     */
    @Column(name = "scheduled_at", nullable = false)
    private Instant scheduledAt;
    
    /**
     * A tag or label classifying the field visit (e.g., Routine, Emergency).
     */
    @Column(name = "tag", length = 50)
    private String tag;
    
    /**
     * The title of the visit note or report.
     */
    @Column(name = "note_title", length = 200)
    private String noteTitle;
    
    /**
     * The detailed body/content of the visit note.
     */
    @Column(name = "note_body", columnDefinition = "TEXT")
    private String noteBody;
    
    /**
     * Flag indicating whether the field visit is urgent.
     */
    @Column(name = "urgent", nullable = false)
    private Boolean urgent;
    
    /**
     * Flag indicating whether the field visit has been completed.
     */
    @Column(name = "completed", nullable = false)
    private Boolean completed;
    
    /**
     * The timestamp indicating when the field visit was marked as completed.
     */
    @Column(name = "completed_at")
    private Instant completedAt;
    
    /**
     * The latitude coordinate of the location where the field visit was recorded.
     */
    @Column(name = "latitude")
    private Double latitude;
    
    /**
     * The longitude coordinate of the location where the field visit was recorded.
     */
    @Column(name = "longitude")
    private Double longitude;
    
    /**
     * The base64-encoded string representation of a photo taken during the field visit.
     */
    @Column(name = "photo_base64", columnDefinition = "TEXT")
    private String photoBase64;
}
