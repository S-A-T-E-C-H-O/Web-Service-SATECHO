package com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.analytics.domain.model.queries.ParcelComparison;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

import java.util.List;

public interface ParcelComparisonQueryService {
    /** Compares up to 4 zones side by side (EP-013-US002). */
    Result<List<ParcelComparison>, ApplicationError> compare(List<Long> zoneIds);
}
