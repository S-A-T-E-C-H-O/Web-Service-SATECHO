package com.satecho.agrosafe.platform.apiservice.iam.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;

public interface UserCommandService {
    Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);
    Result<User, ApplicationError> handle(SignUpCommand command);
    Result<User, ApplicationError> verifyAccount(VerifyAccountCommand command);
    Result<Void, ApplicationError> resendVerification(ResendVerificationCommand command);
}