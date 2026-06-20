package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources.BatchIngestResource;
import java.util.List;

public class BatchIngestCommandFromResourceAssembler {
    private BatchIngestCommandFromResourceAssembler() {}
    public static BatchIngestCommand toCommandFromResources(List<BatchIngestResource> resources) {
        return new BatchIngestCommand(resources.stream()
                .map(r -> new IngestTelemetryCommand(r.deviceId(), r.zoneId(), r.metricType(), r.value(), r.timestamp())).toList());
    }
}
