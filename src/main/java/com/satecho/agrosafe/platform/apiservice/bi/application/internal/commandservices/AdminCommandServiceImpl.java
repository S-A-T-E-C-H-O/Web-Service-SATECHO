package com.satecho.agrosafe.platform.apiservice.bi.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.bi.application.commandservices.AdminCommandService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.exceptions.UserNotSuspendedException;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.ReactivateUserCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.SuspendUserCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.events.UserReactivatedEvent;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.events.UserSuspendedEvent;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.SuspendedAccountRepository;
import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminCommandServiceImpl implements AdminCommandService {

    private final SuspendedAccountRepository suspendedAccountRepository;
    private final UserPersistenceRepository userRepository;

    public AdminCommandServiceImpl(SuspendedAccountRepository suspendedAccountRepository,
                                   UserPersistenceRepository userRepository) {
        this.suspendedAccountRepository = suspendedAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Result<SuspendedAccount, ApplicationError> suspendUser(SuspendUserCommand command) {
        if (!userRepository.existsById(command.userId())) {
            return Result.failure(ApplicationError.notFound("User", String.valueOf(command.userId())));
        }

        suspendedAccountRepository.findActiveSuspensionByUserId(command.userId())
                .ifPresent(sa -> {
                    throw new IllegalStateException("User is already suspended");
                });

        SuspendedAccount suspended = new SuspendedAccount(
                command.userId(), command.reason(), command.suspendedBy());
        SuspendedAccount saved = suspendedAccountRepository.save(suspended);

        saved.addDomainEvent(new UserSuspendedEvent(
                command.userId(), command.reason(), command.suspendedBy()));

        return Result.success(saved);
    }

    @Override
    public Result<SuspendedAccount, ApplicationError> reactivateUser(ReactivateUserCommand command) {
        if (!userRepository.existsById(command.userId())) {
            return Result.failure(ApplicationError.notFound("User", String.valueOf(command.userId())));
        }

        SuspendedAccount suspended = suspendedAccountRepository
                .findActiveSuspensionByUserId(command.userId())
                .orElseThrow(() -> new UserNotSuspendedException(command.userId()));

        suspended.reactivate();
        SuspendedAccount saved = suspendedAccountRepository.save(suspended);

        saved.addDomainEvent(new UserReactivatedEvent(
                command.userId(), command.reactivatedBy()));

        return Result.success(saved);
    }
}
