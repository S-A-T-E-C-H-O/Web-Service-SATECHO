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
@Table(name = "client_assignments")
@Getter
@Setter
@NoArgsConstructor
public class ClientAssignmentPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "agronomist_user_id", nullable = false)
    private Long agronomistUserId;
    @Column(name = "farmer_user_id", nullable = false)
    private Long farmerUserId;
    @Column(name = "assigned_at", nullable = false)
    private Instant assignedAt;
    @Column(name = "active", nullable = false)
    private Boolean active;
}
