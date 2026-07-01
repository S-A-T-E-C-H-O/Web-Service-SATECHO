package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AgronomistClientRepository;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers.AgronomistClientPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories.AgronomistClientPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AgronomistClientRepositoryImpl implements AgronomistClientRepository {

    private final AgronomistClientPersistenceRepository persistenceRepository;

    public AgronomistClientRepositoryImpl(AgronomistClientPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public AgronomistClient save(AgronomistClient client) {
        var saved = persistenceRepository.save(AgronomistClientPersistenceAssembler.toPersistenceFromDomain(client));
        return AgronomistClientPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<AgronomistClient> findById(Long id) {
        return persistenceRepository.findById(id).map(AgronomistClientPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<AgronomistClient> findByAgronomistIdAndActiveTrue(Long agronomistId) {
        return persistenceRepository.findByAgronomistIdAndActiveTrue(agronomistId)
                .stream().map(AgronomistClientPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<AgronomistClient> findByFarmerIdAndActiveTrue(Long farmerId) {
        return persistenceRepository.findByFarmerIdAndActiveTrue(farmerId)
                .map(AgronomistClientPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<AgronomistClient> findByAgronomistIdAndFarmerIdAndActiveTrue(Long agronomistId, Long farmerId) {
        return persistenceRepository.findByAgronomistIdAndFarmerIdAndActiveTrue(agronomistId, farmerId)
                .map(AgronomistClientPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public long countByAgronomistId(Long agronomistId) {
        return persistenceRepository.countByAgronomistId(agronomistId);
    }

    @Override
    public long countByAgronomistIdAndActiveTrue(Long agronomistId) {
        return persistenceRepository.countByAgronomistIdAndActiveTrue(agronomistId);
    }
}
