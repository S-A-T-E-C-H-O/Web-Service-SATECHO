package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.FirmwareResource;

public class FirmwareResourceFromEntityAssembler {

    private FirmwareResourceFromEntityAssembler() {
    }

    public static FirmwareResource toResourceFromEntity(FirmwareRelease entity) {
        return new FirmwareResource(
                entity.getId(),
                entity.getVersion(),
                entity.getDeviceModel(),
                entity.getChangelog(),
                entity.getBinaryUrl(),
                entity.getReleasedAt(),
                entity.isActive(),
                null
        );
    }
}
