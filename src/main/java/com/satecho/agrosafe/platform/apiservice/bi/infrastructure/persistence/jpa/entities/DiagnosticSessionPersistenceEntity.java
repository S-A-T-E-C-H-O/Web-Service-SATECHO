package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "diagnostic_sessions")
@Getter
@Setter
@NoArgsConstructor
public class DiagnosticSessionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(nullable = false, length = 30)
    private String status;

    @Column(name = "component_results", columnDefinition = "TEXT")
    private String componentResults;

    @Column(columnDefinition = "TEXT")
    private String recommendation;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "completed_at")
    private Instant completedAt;
}
