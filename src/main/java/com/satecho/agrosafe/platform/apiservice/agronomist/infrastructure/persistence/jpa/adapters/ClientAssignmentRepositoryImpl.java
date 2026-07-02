package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers.ClientAssignmentPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories.ClientAssignmentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientAssignmentRepositoryImpl implements ClientAssignmentRepository {

    private final ClientAssignmentPersistenceRepository persistenceRepository;

    public ClientAssignmentRepositoryImpl(ClientAssignmentPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public ClientAssignment save(ClientAssignment assignment) {
        var saved = persistenceRepository.save(ClientAssignmentPersistenceAssembler.toPersistenceFromDomain(assignment));
        return ClientAssignmentPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<ClientAssignment> findByAgronomistUserId(Long agronomistUserId) {
        return persistenceRepository.findByAgronomistUserId(agronomistUserId).stream()
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<ClientAssignment> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId) {
        return persistenceRepository.findByAgronomistUserIdAndFarmerUserId(agronomistUserId, farmerUserId)
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<ClientAssignment> findByFarmerUserId(Long farmerUserId) {
        return persistenceRepository.findByFarmerUserId(farmerUserId)
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId) {
        return persistenceRepository.existsByAgronomistUserIdAndFarmerUserId(agronomistUserId, farmerUserId);
    }
}
