package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.IngestSecurityEventCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.IngestSecurityEventResource;
import java.time.Instant;

public class IngestSecurityEventCommandFromResourceAssembler {
    private IngestSecurityEventCommandFromResourceAssembler() {}
    public static IngestSecurityEventCommand toCommand(IngestSecurityEventResource resource) {
        EventClassification classification = EventClassification.valueOf(resource.classification());
        Instant detectedAt = resource.detectedAt() != null ? resource.detectedAt() : Instant.now();
        return new IngestSecurityEventCommand(resource.farmId(), resource.deviceId(), classification,
                resource.confidenceLevel(), detectedAt, resource.locationDescription(), resource.rawData());
    }
}
