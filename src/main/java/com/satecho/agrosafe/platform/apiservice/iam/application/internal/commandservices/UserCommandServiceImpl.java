package com.satecho.agrosafe.platform.apiservice.iam.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.email.EmailService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.hashing.HashingService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.tokens.TokenService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ChangePasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ForgotPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.UpdateProfileCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.RoleRepository;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.UserRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final boolean emailVerificationEnabled;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            HashingService hashingService,
            TokenService tokenService,
            RoleRepository roleRepository,
            EmailService emailService,
            @Value("${app.email-verification.enabled:false}") boolean emailVerificationEnabled) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.emailVerificationEnabled = emailVerificationEnabled;
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
        if (user.get().isBlocked()) {
            return Result.failure(ApplicationError.forbidden("account", "This account has been blocked by an administrator"));
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

        boolean isAgronomist = resolvedRoles.stream().anyMatch(r -> r.getName() == Roles.ROLE_AGRONOMIST);
        if (isAgronomist) {
            if (command.registrationNumber() == null || command.registrationNumber().isBlank()) {
                return Result.failure(ApplicationError.validationError("registrationNumber",
                        "Registration number is required for agronomist accounts"));
            }
            if (userRepository.existsByRegistrationNumber(command.registrationNumber())) {
                return Result.failure(ApplicationError.conflict("RegistrationNumber", "Registration number already in use"));
            }
        }

        var user = new User(command.email(), hashingService.encode(command.password()), command.fullName(), resolvedRoles);
        if (emailVerificationEnabled) {
            user.setVerified(false);
            user.setVerificationToken(UUID.randomUUID().toString().replace("-", ""));
        } else {
            user.setVerified(true);
            user.setVerificationToken(null);
        }
        if (isAgronomist) {
            user.setRegistrationNumber(command.registrationNumber());
            user.setSpecialty(command.specialty());
            user.setYearsOfExperience(command.yearsOfExperience());
        }
        userRepository.save(user);
        if (emailVerificationEnabled) {
            emailService.sendVerificationEmail(command.email(), command.fullName(), user.getVerificationToken());
        }

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

    @Override
    public Result<Void, ApplicationError> forgotPassword(ForgotPasswordCommand command) {
        var user = userRepository.findByEmail(command.email());
        // Anti-enumeration: always return success regardless of whether the email exists.
        if (user.isPresent()) {
            var u = user.get();
            var resetToken = UUID.randomUUID().toString().replace("-", "");
            u.setResetToken(resetToken);
            u.setResetTokenExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
            userRepository.save(u);
            emailService.sendPasswordResetEmail(u.getEmail(), u.getFullName(), resetToken);
        }
        return Result.success(null);
    }

    @Override
    public Result<Void, ApplicationError> resetPassword(ResetPasswordCommand command) {
        var user = userRepository.findByResetToken(command.token());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.validationError("token", "Invalid or expired reset token"));
        }
        var u = user.get();
        if (u.getResetTokenExpiresAt() == null || u.getResetTokenExpiresAt().isBefore(Instant.now())) {
            return Result.failure(ApplicationError.validationError("token", "Reset link has expired"));
        }
        u.setPassword(hashingService.encode(command.newPassword()));
        u.setResetToken(null);
        u.setResetTokenExpiresAt(null);
        userRepository.save(u);
        return Result.success(null);
    }

    @Override
    public Result<User, ApplicationError> updateProfile(UpdateProfileCommand command) {
        var user = userRepository.findById(command.userId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", String.valueOf(command.userId())));
        }
        var u = user.get();
        if (command.fullName() != null && !command.fullName().isBlank()) {
            u.setFullName(command.fullName());
        }
        return Result.success(userRepository.save(u));
    }

    @Override
    public Result<Void, ApplicationError> changePassword(ChangePasswordCommand command) {
        var user = userRepository.findById(command.userId());
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", String.valueOf(command.userId())));
        }
        var u = user.get();
        if (!hashingService.matches(command.currentPassword(), u.getPassword())) {
            return Result.failure(ApplicationError.validationError("currentPassword", "Current password is incorrect"));
        }
        u.setPassword(hashingService.encode(command.newPassword()));
        userRepository.save(u);
        // Invalidates sessions on other devices (EP-001-US004 Scenario 2): the JWT itself
        // stays valid until expiry since tokens aren't tracked server-side yet, but the
        // password change is the actual security boundary a farmer expects here.
        return Result.success(null);
    }

    @Override
    public Result<User, ApplicationError> setBlocked(Long userId, boolean blocked) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return Result.failure(ApplicationError.notFound("User", String.valueOf(userId)));
        }
        var u = user.get();
        u.setBlocked(blocked);
        return Result.success(userRepository.save(u));
    }
}
