package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** An agronomist's written recommendation to a farmer (EP-009-US004). */
@Getter
public class Recommendation extends AuditableAbstractAggregateRoot<Recommendation> {
    @Setter private Long id;
    @Setter private Long agronomistUserId;
    @Setter private Long farmerUserId;
    @Setter private Long zoneId;
    @Setter private String title;
    @Setter private String description;
    @Setter private String recommendedActions;
    @Setter private String attachmentUrl;
    @Setter private RecommendationPriority priority;
    @Setter private RecommendationStatus status;
    @Setter private Instant generatedAt;
    @Setter private Instant resolvedAt;

    public Recommendation() {}

    public Recommendation(Long agronomistUserId, Long farmerUserId, Long zoneId, String title, String description,
                           String recommendedActions, String attachmentUrl, RecommendationPriority priority) {
        if (agronomistUserId == null) throw new IllegalArgumentException("agronomistUserId cannot be null");
        if (farmerUserId == null) throw new IllegalArgumentException("farmerUserId cannot be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("title cannot be null or blank");
        this.agronomistUserId = agronomistUserId;
        this.farmerUserId = farmerUserId;
        this.zoneId = zoneId;
        this.title = title;
        this.description = description;
        this.recommendedActions = recommendedActions;
        this.attachmentUrl = attachmentUrl;
        this.priority = priority != null ? priority : RecommendationPriority.MEDIUM;
        this.status = RecommendationStatus.SENT;
        this.generatedAt = Instant.now();
    }

    public void acknowledge() {
        this.status = RecommendationStatus.COMPLETED;
        this.resolvedAt = Instant.now();
    }

    public void dismiss() {
        this.status = RecommendationStatus.DISMISSED;
        this.resolvedAt = Instant.now();
    }
}
