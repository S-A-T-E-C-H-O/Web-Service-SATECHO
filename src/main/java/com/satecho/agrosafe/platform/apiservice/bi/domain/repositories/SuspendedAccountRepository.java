package com.satecho.agrosafe.platform.apiservice.bi.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;

import java.util.Optional;

public interface SuspendedAccountRepository {
    Optional<SuspendedAccount> findActiveSuspensionByUserId(Long userId);
    SuspendedAccount save(SuspendedAccount suspendedAccount);
}
