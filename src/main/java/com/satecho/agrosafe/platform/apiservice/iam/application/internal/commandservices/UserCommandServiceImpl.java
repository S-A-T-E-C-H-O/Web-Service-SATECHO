package com.satecho.agrosafe.platform.apiservice.iam.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.email.EmailService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.hashing.HashingService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.tokens.TokenService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.RoleRepository;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.UserRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            HashingService hashingService,
            TokenService tokenService,
            RoleRepository roleRepository,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Override
    public Result<ImmutablePair<User, String>, ApplicationError> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", command.email()));
        }
        if (!hashingService.matches(command.password(), user.get().getPassword())) {
            return Result.failure(ApplicationError.validationError("credentials", "Invalid username or password"));
        }
        if (Boolean.FALSE.equals(user.get().getVerified())) {
            return Result.failure(ApplicationError.forbidden("account", "Email verification required before signing in"));
        }
        var token = tokenService.generateToken(user.get().getEmail());
        return Result.success(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Result<User, ApplicationError> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            return Result.failure(ApplicationError.conflict("User", "Username already exists"));
        }
        var roles = command.roles().stream()
                .map(role -> roleRepository.findByName(role.getName()))
                .toList();

        if (roles.stream().anyMatch(java.util.Optional::isEmpty)) {
            return Result.failure(ApplicationError.notFound("Role", "one or more role names"));
        }

        var resolvedRoles = roles.stream()
                .map(java.util.Optional::get)
                .toList();

        var verificationToken = UUID.randomUUID().toString().replace("-", "");
        var user = new User(command.email(), hashingService.encode(command.password()), command.fullName(), resolvedRoles);
        user.setVerificationToken(verificationToken);
        userRepository.save(user);
        emailService.sendVerificationEmail(command.email(), command.fullName(), verificationToken);

        return userRepository.findByEmail(command.email())
                .<Result<User, ApplicationError>>map(Result::success)
                .orElseGet(() -> Result.failure(ApplicationError.unexpected("sign-up", "Created user could not be reloaded")));
    }

    @Override
    public Result<User, ApplicationError> verifyAccount(VerifyAccountCommand command) {
        var user = userRepository.findByVerificationToken(command.token());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("VerificationToken", command.token()));
        }
        var u = user.get();
        u.setVerified(true);
        u.setVerificationToken(null);
        var saved = userRepository.save(u);
        return Result.success(saved);
    }

    @Override
    public Result<Void, ApplicationError> resendVerification(ResendVerificationCommand command) {
        var user = userRepository.findByEmail(command.email());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", command.email()));
        }
        if (Boolean.TRUE.equals(user.get().getVerified())) {
            return Result.failure(ApplicationError.conflict("account", "Account is already verified"));
        }
        var u = user.get();
        var verificationToken = UUID.randomUUID().toString().replace("-", "");
        u.setVerificationToken(verificationToken);
        userRepository.save(u);
        emailService.sendVerificationEmail(u.getEmail(), u.getFullName(), verificationToken);
        return Result.success(null);
    }
}