package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationPriority;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects.RecommendationStatus;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * An agronomist's written recommendation to a farmer (EP-009-US004).
 *
 * @author Colegio
 * @version 1.0
 */
@Getter
public class Recommendation extends AuditableAbstractAggregateRoot<Recommendation> {
    /**
     * The unique identifier of the recommendation.
     */
    @Setter private Long id;

    /**
     * The identifier of the agronomist user.
     */
    @Setter private Long agronomistUserId;

    /**
     * The identifier of the farmer user.
     */
    @Setter private Long farmerUserId;

    /**
     * The identifier of the zone.
     */
    @Setter private Long zoneId;

    /**
     * The title of the recommendation.
     */
    @Setter private String title;

    /**
     * The description of the recommendation.
     */
    @Setter private String description;

    /**
     * The recommended actions to be taken.
     */
    @Setter private String recommendedActions;

    /**
     * The URL of any attachments.
     */
    @Setter private String attachmentUrl;

    /**
     * The priority of the recommendation.
     */
    @Setter private RecommendationPriority priority;

    /**
     * The status of the recommendation.
     */
    @Setter private RecommendationStatus status;

    /**
     * The timestamp when the recommendation was generated.
     */
    @Setter private Instant generatedAt;

    /**
     * The timestamp when the recommendation was resolved.
     */
    @Setter private Instant resolvedAt;

    /**
     * Default constructor for Recommendation.
     */
    public Recommendation() {}

    /**
     * Parameterized constructor for Recommendation.
     *
     * @param agronomistUserId   the ID of the agronomist user, must not be null
     * @param farmerUserId       the ID of the farmer user, must not be null
     * @param zoneId             the ID of the zone
     * @param title              the title of the recommendation, must not be null or blank
     * @param description        the description of the recommendation
     * @param recommendedActions the recommended actions
     * @param attachmentUrl      the attachment URL
     * @param priority           the priority of the recommendation; if null, defaults to MEDIUM
     * @throws IllegalArgumentException if agronomistUserId, farmerUserId, or title is null/blank
     */
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

    /**
     * Acknowledges the recommendation, updating its status to COMPLETED and recording the resolution timestamp.
     */
    public void acknowledge() {
        this.status = RecommendationStatus.COMPLETED;
        this.resolvedAt = Instant.now();
    }

    /**
     * Dismisses the recommendation, updating its status to DISMISSED and recording the resolution timestamp.
     */
    public void dismiss() {
        this.status = RecommendationStatus.DISMISSED;
        this.resolvedAt = Instant.now();
    }
}
