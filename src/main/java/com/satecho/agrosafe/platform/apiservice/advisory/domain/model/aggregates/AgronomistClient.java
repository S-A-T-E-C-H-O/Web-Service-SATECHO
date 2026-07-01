package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class AgronomistClient extends AuditableAbstractAggregateRoot<AgronomistClient> {
    @Setter private Long id;
    @Setter private Long agronomistId;
    @Setter private Long farmerId;
    @Setter private Instant linkedAt;
    @Setter private Boolean active;
    @Setter private String notes;

    public AgronomistClient() {}

    public AgronomistClient(Long agronomistId, Long farmerId, String notes) {
        this.agronomistId = agronomistId;
        this.farmerId = farmerId;
        this.linkedAt = Instant.now();
        this.active = true;
        this.notes = notes;
    }

    public void unlink() {
        this.active = false;
    }

    public void updateNotes(String notes) {
        this.notes = notes;
    }
}
