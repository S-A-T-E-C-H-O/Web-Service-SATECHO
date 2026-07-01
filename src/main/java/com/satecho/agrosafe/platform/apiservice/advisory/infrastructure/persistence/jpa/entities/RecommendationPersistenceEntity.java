package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "recommendations")
@Getter
@Setter
@NoArgsConstructor
public class RecommendationPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "zone_id")
    private Long zoneId;

    @Column(name = "agronomist_id", nullable = false)
    private Long agronomistId;

    @Column(name = "farmer_id", nullable = false)
    private Long farmerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecommendationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private RecommendationPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecommendationStatus status;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(name = "recommended_actions", nullable = false, length = 2000)
    private String recommendedActions;

    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "acknowledged_at")
    private Instant acknowledgedAt;

    @Column(name = "acknowledged_by")
    private Long acknowledgedBy;
}
