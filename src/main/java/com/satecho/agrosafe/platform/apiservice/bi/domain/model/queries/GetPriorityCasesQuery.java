package com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries;

public record GetPriorityCasesQuery(
        Integer limit
) {
    public GetPriorityCasesQuery {
        if (limit == null || limit <= 0) {
            limit = 10;
        }
    }
}
