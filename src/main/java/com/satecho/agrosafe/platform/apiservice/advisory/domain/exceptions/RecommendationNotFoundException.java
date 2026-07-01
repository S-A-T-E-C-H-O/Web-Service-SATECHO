package com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions;

import com.satecho.agrosafe.platform.apiservice.shared.domain.exception.AgroSafeException;

public class RecommendationNotFoundException extends AgroSafeException {

    public RecommendationNotFoundException(Long recommendationId) {
        super("Recommendation not found with ID: " + recommendationId);
    }
}
