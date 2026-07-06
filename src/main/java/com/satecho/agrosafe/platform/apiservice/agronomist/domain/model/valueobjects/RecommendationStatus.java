package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.valueobjects;

/**
 * Enumeration representing the possible statuses of a recommendation.
 *
 * @author Colegio
 * @version 1.0
 */
public enum RecommendationStatus {
    /**
     * The recommendation has been sent to the farmer.
     */
    SENT,

    /**
     * The recommendation has been completed or acknowledged by the farmer.
     */
    COMPLETED,

    /**
     * The recommendation has been dismissed by the farmer.
     */
    DISMISSED
}
