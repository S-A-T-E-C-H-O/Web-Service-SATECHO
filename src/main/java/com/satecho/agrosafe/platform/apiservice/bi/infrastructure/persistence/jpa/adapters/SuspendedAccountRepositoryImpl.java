package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.SuspendedAccountRepository;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers.SuspendedAccountPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories.SuspendedAccountPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SuspendedAccountRepositoryImpl implements SuspendedAccountRepository {

    private final SuspendedAccountPersistenceRepository persistenceRepository;

    public SuspendedAccountRepositoryImpl(SuspendedAccountPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<SuspendedAccount> findActiveSuspensionByUserId(Long userId) {
        return persistenceRepository.findActiveSuspensionByUserId(userId)
                .map(SuspendedAccountPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public SuspendedAccount save(SuspendedAccount suspendedAccount) {
        var saved = persistenceRepository.save(SuspendedAccountPersistenceAssembler.toPersistenceFromDomain(suspendedAccount));
        return SuspendedAccountPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
