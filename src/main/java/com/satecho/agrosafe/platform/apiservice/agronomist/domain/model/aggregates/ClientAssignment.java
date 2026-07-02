package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/** Links an agronomist to a farmer they advise (EP-009-TS001). */
@Getter
public class ClientAssignment extends AuditableAbstractAggregateRoot<ClientAssignment> {
    @Setter private Long id;
    @Setter private Long agronomistUserId;
    @Setter private Long farmerUserId;
    @Setter private Instant assignedAt;
    @Setter private Boolean active;

    public ClientAssignment() {}

    public ClientAssignment(Long agronomistUserId, Long farmerUserId) {
        if (agronomistUserId == null) throw new IllegalArgumentException("agronomistUserId cannot be null");
        if (farmerUserId == null) throw new IllegalArgumentException("farmerUserId cannot be null");
        this.agronomistUserId = agronomistUserId;
        this.farmerUserId = farmerUserId;
        this.assignedAt = Instant.now();
        this.active = true;
    }
}
