package com.satecho.agrosafe.platform.apiservice.activitylog.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "activity_log_entries")
@Getter
@Setter
@NoArgsConstructor
public class ActivityLogEntryPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "farm_id", nullable = false)
    private Long farmId;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private ActivityType type;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;
}
