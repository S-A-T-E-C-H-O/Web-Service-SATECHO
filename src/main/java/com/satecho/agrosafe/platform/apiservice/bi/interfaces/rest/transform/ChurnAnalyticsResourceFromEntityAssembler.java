package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.ChurnAnalyticsResource;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;

public class ChurnAnalyticsResourceFromEntityAssembler {

    private ChurnAnalyticsResourceFromEntityAssembler() {
    }

    public static ChurnAnalyticsResource toResourceFromEntity(AnalyticsSnapshot entity, String segment) {
        return new ChurnAnalyticsResource(
                entity.getChurnRate(),
                entity.getPeriod().name(),
                entity.getCanceledSubscriptions(),
                entity.getNewSubscriptions(),
                segment,
                entity.getRawData()
        );
    }
}
