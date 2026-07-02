package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;

import java.util.List;
import java.util.Optional;

public interface ClientAssignmentRepository {
    ClientAssignment save(ClientAssignment assignment);
    List<ClientAssignment> findByAgronomistUserId(Long agronomistUserId);
    Optional<ClientAssignment> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
    Optional<ClientAssignment> findByFarmerUserId(Long farmerUserId);
    boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
}
