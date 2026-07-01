package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "agronomist_clients", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"agronomist_id", "farmer_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class AgronomistClientPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "agronomist_id", nullable = false)
    private Long agronomistId;

    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @Column(name = "linked_at", nullable = false)
    private Instant linkedAt;

    @Column(nullable = false)
    private Boolean active;

    @Column(length = 500)
    private String notes;
}
