package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetAllUsersQuery(
        String search,
        String role,
        Integer page,
        Integer size
) {
    public GetAllUsersQuery {
        if (page == null || page < 0) {
            page = 0;
        }
        if (size == null || size <= 0) {
            size = 20;
        }
    }
}
