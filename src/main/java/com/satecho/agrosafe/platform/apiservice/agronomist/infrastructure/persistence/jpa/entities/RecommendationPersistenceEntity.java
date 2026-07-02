package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationStatus;
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
    @Column(name = "agronomist_user_id", nullable = false)
    private Long agronomistUserId;
    @Column(name = "farmer_user_id", nullable = false)
    private Long farmerUserId;
    @Column(name = "zone_id")
    private Long zoneId;
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "recommended_actions", columnDefinition = "TEXT")
    private String recommendedActions;
    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 20)
    private RecommendationPriority priority;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecommendationStatus status;
    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;
    @Column(name = "resolved_at")
    private Instant resolvedAt;
}
