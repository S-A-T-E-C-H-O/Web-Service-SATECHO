package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.SnapshotResource;

import java.time.Instant;

public class SuspendedAccountResourceAssembler {

    private SuspendedAccountResourceAssembler() {
    }

    public static SnapshotResource toSuspendSnapshot(SuspendedAccount entity) {
        return new SnapshotResource(
                entity.getId(),
                "SUSPENSION",
                entity.getSuspendedAt(),
                entity.getReactivatedAt() != null ? entity.getReactivatedAt() : Instant.now(),
                0, 0.0, null, 0.0, 0.0, 0, 0,
                "{\"userId\":" + entity.getUserId() + ",\"reason\":\"" + entity.getReason() + "\"}"
        );
    }
}
