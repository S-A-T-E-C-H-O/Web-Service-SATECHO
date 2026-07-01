package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.RecommendationType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Recommendation extends AuditableAbstractAggregateRoot<Recommendation> {
    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private Long zoneId;
    @Setter private Long agronomistId;
    @Setter private Long farmerId;
    @Setter private RecommendationType type;
    @Setter private RecommendationPriority priority;
    @Setter private RecommendationStatus status;
    @Setter private String title;
    @Setter private String description;
    @Setter private String recommendedActions;
    @Setter private Instant generatedAt;
    @Setter private Instant sentAt;
    @Setter private Instant acknowledgedAt;
    @Setter private Long acknowledgedBy;

    public Recommendation() {}

    public Recommendation(Long farmId, Long zoneId, Long agronomistId, Long farmerId,
                          RecommendationType type, RecommendationPriority priority,
                          String title, String description, String recommendedActions) {
        this.farmId = farmId;
        this.zoneId = zoneId;
        this.agronomistId = agronomistId;
        this.farmerId = farmerId;
        this.type = type;
        this.priority = priority;
        this.status = RecommendationStatus.PENDING;
        this.title = title;
        this.description = description;
        this.recommendedActions = recommendedActions;
        this.generatedAt = Instant.now();
    }

    public void send() {
        this.status = RecommendationStatus.SENT;
        this.sentAt = Instant.now();
    }

    public void acknowledge(Long farmerId) {
        this.status = RecommendationStatus.ACKNOWLEDGED;
        this.acknowledgedAt = Instant.now();
        this.acknowledgedBy = farmerId;
    }

    public void dismiss() {
        this.status = RecommendationStatus.DISMISSED;
    }

    public void markAsImplemented() {
        this.status = RecommendationStatus.IMPLEMENTED;
    }
}
