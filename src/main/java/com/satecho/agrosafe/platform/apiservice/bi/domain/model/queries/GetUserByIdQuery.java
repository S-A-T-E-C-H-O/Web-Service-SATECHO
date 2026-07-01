package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetUserByIdQuery(
        Long userId
) {
    public GetUserByIdQuery {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
    }
}
