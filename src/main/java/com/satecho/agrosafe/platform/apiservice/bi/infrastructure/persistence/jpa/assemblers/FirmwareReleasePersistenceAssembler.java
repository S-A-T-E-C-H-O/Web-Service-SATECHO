package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.FirmwareReleasePersistenceEntity;

public final class FirmwareReleasePersistenceAssembler {

    private FirmwareReleasePersistenceAssembler() {
    }

    public static FirmwareRelease toDomainFromPersistence(FirmwareReleasePersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new FirmwareRelease();
        domain.setId(entity.getId());
        domain.setVersion(entity.getVersion());
        domain.setDeviceModel(entity.getDeviceModel());
        domain.setChangelog(entity.getChangelog());
        domain.setBinaryUrl(entity.getBinaryUrl());
        domain.setReleasedAt(entity.getReleasedAt());
        domain.setActive(entity.isActive());
        return domain;
    }

    public static FirmwareReleasePersistenceEntity toPersistenceFromDomain(FirmwareRelease domain) {
        if (domain == null) return null;
        var entity = new FirmwareReleasePersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setVersion(domain.getVersion());
        entity.setDeviceModel(domain.getDeviceModel());
        entity.setChangelog(domain.getChangelog());
        entity.setBinaryUrl(domain.getBinaryUrl());
        entity.setReleasedAt(domain.getReleasedAt());
        entity.setActive(domain.isActive());
        return entity;
    }
}
