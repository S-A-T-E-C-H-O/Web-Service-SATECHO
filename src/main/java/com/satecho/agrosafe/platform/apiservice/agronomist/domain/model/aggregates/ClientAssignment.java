package com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Links an agronomist to a farmer they advise (EP-009-TS001).
 *
 * @author Colegio
 * @version 1.0
 */
@Getter
public class ClientAssignment extends AuditableAbstractAggregateRoot<ClientAssignment> {
    /**
     * The unique identifier of the client assignment.
     */
    @Setter private Long id;

    /**
     * The identifier of the agronomist user.
     */
    @Setter private Long agronomistUserId;

    /**
     * The identifier of the farmer user.
     */
    @Setter private Long farmerUserId;

    /**
     * The timestamp when the assignment was made.
     */
    @Setter private Instant assignedAt;

    /**
     * Flag indicating whether the assignment is currently active.
     */
    @Setter private Boolean active;

    /**
     * Default constructor for ClientAssignment.
     */
    public ClientAssignment() {}

    /**
     * Parameterized constructor for ClientAssignment.
     *
     * @param agronomistUserId the ID of the agronomist user, must not be null
     * @param farmerUserId the ID of the farmer user, must not be null
     * @throws IllegalArgumentException if agronomistUserId or farmerUserId is null
     */
    public ClientAssignment(Long agronomistUserId, Long farmerUserId) {
        if (agronomistUserId == null) throw new IllegalArgumentException("agronomistUserId cannot be null");
        if (farmerUserId == null) throw new IllegalArgumentException("farmerUserId cannot be null");
        this.agronomistUserId = agronomistUserId;
        this.farmerUserId = farmerUserId;
        this.assignedAt = Instant.now();
        this.active = true;
    }
}
