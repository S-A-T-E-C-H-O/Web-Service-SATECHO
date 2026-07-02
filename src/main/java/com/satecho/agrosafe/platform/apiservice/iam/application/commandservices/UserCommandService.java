package com.satecho.agrosafe.platform.apiservice.iam.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ChangePasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ForgotPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.UpdateProfileCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;

public interface UserCommandService {
    Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command);
    Result<User, ApplicationError> handle(SignUpCommand command);
    Result<User, ApplicationError> verifyAccount(VerifyAccountCommand command);
    Result<Void, ApplicationError> resendVerification(ResendVerificationCommand command);
    Result<Void, ApplicationError> forgotPassword(ForgotPasswordCommand command);
    Result<Void, ApplicationError> resetPassword(ResetPasswordCommand command);
    Result<User, ApplicationError> updateProfile(UpdateProfileCommand command);
    Result<Void, ApplicationError> changePassword(ChangePasswordCommand command);
    Result<User, ApplicationError> setBlocked(Long userId, boolean blocked);
}