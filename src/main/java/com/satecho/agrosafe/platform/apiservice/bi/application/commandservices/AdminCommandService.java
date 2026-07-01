package com.satecho.agrosafe.platform.apiservice.bi.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.ReactivateUserCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.SuspendUserCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface AdminCommandService {
    Result<SuspendedAccount, ApplicationError> suspendUser(SuspendUserCommand command);
    Result<SuspendedAccount, ApplicationError> reactivateUser(ReactivateUserCommand command);
}
