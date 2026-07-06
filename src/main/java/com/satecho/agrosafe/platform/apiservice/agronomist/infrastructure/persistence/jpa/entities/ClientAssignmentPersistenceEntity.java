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
 * Persistence entity representing a client assignment in the database.
 * <p>
 * This class maps the {@code client_assignments} table and stores the association
 * between an agronomist and a farmer.
 * </p>
 */
@Entity
@Table(name = "client_assignments")
@Getter
@Setter
@NoArgsConstructor
public class ClientAssignmentPersistenceEntity extends AuditableAbstractPersistenceEntity {
    
    /**
     * The ID of the agronomist user assigned to the client.
     */
    @Column(name = "agronomist_user_id", nullable = false)
    private Long agronomistUserId;
    
    /**
     * The ID of the farmer user client assigned to the agronomist.
     */
    @Column(name = "farmer_user_id", nullable = false)
    private Long farmerUserId;
    
    /**
     * The timestamp indicating when the assignment was made.
     */
    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
    
    /**
     * Flag indicating whether the assignment is currently active.
     */
    @Column(name = "active", nullable = false)
    private Boolean active;
}
