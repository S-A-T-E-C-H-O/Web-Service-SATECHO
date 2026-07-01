package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.ExecutiveAnalyticsResource;

public class ExecutiveAnalyticsResourceFromEntityAssembler {

    private ExecutiveAnalyticsResourceFromEntityAssembler() {
    }

    public static ExecutiveAnalyticsResource toResourceFromEntity(AnalyticsSnapshot entity) {
        return new ExecutiveAnalyticsResource(
                entity.getId(),
                entity.getPeriod().name(),
                entity.getFromDate(),
                entity.getToDate(),
                entity.getActiveUsers(),
                entity.getMrr(),
                entity.getCurrency(),
                entity.getConversionRate(),
                entity.getChurnRate(),
                entity.getNewSubscriptions(),
                entity.getCanceledSubscriptions(),
                entity.getRawData()
        );
    }
}
