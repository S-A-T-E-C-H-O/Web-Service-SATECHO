package com.satecho.agrosafe.platform.iam.application;

import com.satecho.agrosafe.platform.apiservice.iam.application.internal.commandservices.UserCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.email.EmailService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.hashing.HashingService;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.tokens.TokenService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignInCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.SignUpCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.entities.Role;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.RoleRepository;
import com.satecho.agrosafe.platform.apiservice.iam.domain.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserCommandServiceImpl")
class UserCommandServiceImplTest {

    @Mock UserRepository userRepository;
    @Mock HashingService hashingService;
    @Mock TokenService tokenService;
    @Mock RoleRepository roleRepository;
    @Mock EmailService emailService;

    @InjectMocks UserCommandServiceImpl service;

    // ── SignInCommand ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("handle(SignInCommand): user not found returns failure")
    void signIn_userNotFound_returnsFailure() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        var result = service.handle(new SignInCommand("unknown@example.com", "pass"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignInCommand): wrong password returns failure")
    void signIn_wrongPassword_returnsFailure() {
        User user = new User("user@example.com", "hashed", "Test User");
        user.setVerified(true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("wrong", "hashed")).thenReturn(false);
        var result = service.handle(new SignInCommand("user@example.com", "wrong"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignInCommand): unverified user returns failure")
    void signIn_unverifiedUser_returnsFailure() {
        User user = new User("user@example.com", "hashed", "Test User");
        user.setVerified(false);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("pass", "hashed")).thenReturn(true);
        var result = service.handle(new SignInCommand("user@example.com", "pass"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignInCommand): valid credentials for verified user returns success with token")
    void signIn_validCredentials_returnsSuccessWithToken() {
        User user = new User("user@example.com", "hashed", "Test User");
        user.setVerified(true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("pass", "hashed")).thenReturn(true);
        when(tokenService.generateToken("user@example.com")).thenReturn("jwt-token");

        var result = service.handle(new SignInCommand("user@example.com", "pass"));

        assertThat(result.isSuccess()).isTrue();
        ImmutablePair<User, String> pair = result.toOptional().get();
        assertThat(pair.getRight()).isEqualTo("jwt-token");
        assertThat(pair.getLeft()).isEqualTo(user);
    }

    // ── SignUpCommand ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("handle(SignUpCommand): email already taken returns failure")
    void signUp_emailAlreadyTaken_returnsFailure() {
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);
        Role farmerRole = new Role(Roles.ROLE_FARMER);
        var result = service.handle(new SignUpCommand("Name", "taken@example.com", "pass", List.of(farmerRole), null, null, null));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignUpCommand): role not found in repository returns failure")
    void signUp_roleNotFound_returnsFailure() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        Role farmerRole = new Role(Roles.ROLE_FARMER);
        when(roleRepository.findByName(Roles.ROLE_FARMER)).thenReturn(Optional.empty());
        var result = service.handle(new SignUpCommand("Name", "new@example.com", "pass", List.of(farmerRole), null, null, null));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignUpCommand): valid command saves user, sends email, returns success")
    void signUp_valid_savesUserSendsEmailAndReturnsSuccess() {
        Role farmerRole = new Role(Roles.ROLE_FARMER);
        User savedUser = new User("new@example.com", "encoded", "Name", List.of(farmerRole));

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_FARMER)).thenReturn(Optional.of(farmerRole));
        when(hashingService.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.of(savedUser));

        var result = service.handle(new SignUpCommand("Name", "new@example.com", "pass", List.of(farmerRole), null, null, null));

        assertThat(result.isSuccess()).isTrue();
        verify(userRepository).save(any(User.class));
        verify(emailService).sendVerificationEmail(eq("new@example.com"), eq("Name"), anyString());
    }

    @Test
    @DisplayName("handle(SignUpCommand): user not found after save returns failure")
    void signUp_userNotFoundAfterSave_returnsFailure() {
        Role farmerRole = new Role(Roles.ROLE_FARMER);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_FARMER)).thenReturn(Optional.of(farmerRole));
        when(hashingService.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());

        var result = service.handle(new SignUpCommand("Name", "new@example.com", "pass", List.of(farmerRole), null, null, null));

        assertThat(result.isFailure()).isTrue();
    }

    // ── verifyAccount ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("verifyAccount: token not found returns failure")
    void verifyAccount_tokenNotFound_returnsFailure() {
        when(userRepository.findByVerificationToken("bad-token")).thenReturn(Optional.empty());
        var result = service.verifyAccount(new VerifyAccountCommand("bad-token"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("verifyAccount: valid token sets verified=true, clears token, returns success")
    void verifyAccount_validToken_setsVerifiedAndClearsToken() {
        User user = new User("user@example.com", "hashed", "User");
        user.setVerificationToken("valid-token");
        when(userRepository.findByVerificationToken("valid-token")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = service.verifyAccount(new VerifyAccountCommand("valid-token"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(user.getVerified()).isTrue();
        assertThat(user.getVerificationToken()).isNull();
        verify(userRepository).save(user);
    }

    // ── resendVerification ────────────────────────────────────────────────────

    @Test
    @DisplayName("resendVerification: user not found returns failure")
    void resendVerification_userNotFound_returnsFailure() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());
        var result = service.resendVerification(new ResendVerificationCommand("ghost@example.com"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("resendVerification: already verified user returns failure")
    void resendVerification_alreadyVerified_returnsFailure() {
        User user = new User("verified@example.com", "hash", "User");
        user.setVerified(true);
        when(userRepository.findByEmail("verified@example.com")).thenReturn(Optional.of(user));
        var result = service.resendVerification(new ResendVerificationCommand("verified@example.com"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("resendVerification: unverified user saves new token and sends email, returns success")
    void resendVerification_unverifiedUser_sendsEmailAndReturnsSuccess() {
        User user = new User("unverified@example.com", "hash", "User");
        user.setVerified(false);
        user.setEmail("unverified@example.com");
        user.setFullName("User");
        when(userRepository.findByEmail("unverified@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = service.resendVerification(new ResendVerificationCommand("unverified@example.com"));

        assertThat(result.isSuccess()).isTrue();
        verify(userRepository).save(user);
        verify(emailService).sendVerificationEmail(eq("unverified@example.com"), eq("User"), anyString());
    }

    // ── Agronomist onboarding fields ─────────────────────────────────────────

    @Test
    @DisplayName("handle(SignUpCommand): agronomist without registrationNumber returns failure")
    void signUp_agronomistMissingRegistrationNumber_returnsFailure() {
        Role agronomistRole = new Role(Roles.ROLE_AGRONOMIST);
        when(userRepository.existsByEmail("agro@example.com")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_AGRONOMIST)).thenReturn(Optional.of(agronomistRole));

        var result = service.handle(new SignUpCommand("Agro", "agro@example.com", "pass",
                List.of(agronomistRole), null, "Irrigation", 5));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignUpCommand): agronomist with duplicate registrationNumber returns failure")
    void signUp_agronomistDuplicateRegistrationNumber_returnsFailure() {
        Role agronomistRole = new Role(Roles.ROLE_AGRONOMIST);
        when(userRepository.existsByEmail("agro@example.com")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_AGRONOMIST)).thenReturn(Optional.of(agronomistRole));
        when(userRepository.existsByRegistrationNumber("REG-1")).thenReturn(true);

        var result = service.handle(new SignUpCommand("Agro", "agro@example.com", "pass",
                List.of(agronomistRole), "REG-1", "Irrigation", 5));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("handle(SignUpCommand): valid agronomist saves profile fields and returns success")
    void signUp_validAgronomist_savesProfileFields() {
        Role agronomistRole = new Role(Roles.ROLE_AGRONOMIST);
        User savedUser = new User("agro@example.com", "encoded", "Agro", List.of(agronomistRole));
        when(userRepository.existsByEmail("agro@example.com")).thenReturn(false);
        when(roleRepository.findByName(Roles.ROLE_AGRONOMIST)).thenReturn(Optional.of(agronomistRole));
        when(userRepository.existsByRegistrationNumber("REG-1")).thenReturn(false);
        when(hashingService.encode("pass")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findByEmail("agro@example.com")).thenReturn(Optional.of(savedUser));

        var result = service.handle(new SignUpCommand("Agro", "agro@example.com", "pass",
                List.of(agronomistRole), "REG-1", "Irrigation", 5));

        assertThat(result.isSuccess()).isTrue();
        verify(userRepository).save(argThat(u -> "REG-1".equals(u.getRegistrationNumber())
                && "Irrigation".equals(u.getSpecialty()) && Integer.valueOf(5).equals(u.getYearsOfExperience())));
    }

    // ── forgotPassword / resetPassword ───────────────────────────────────────

    @Test
    @DisplayName("forgotPassword: existing email sets a reset token and sends an email")
    void forgotPassword_existingEmail_sendsEmail() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        when(userRepository.findByEmail("farmer@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = service.forgotPassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ForgotPasswordCommand("farmer@example.com"));

        assertThat(result.isSuccess()).isTrue();
        verify(emailService).sendPasswordResetEmail(eq("farmer@example.com"), eq("Farmer"), anyString());
    }

    @Test
    @DisplayName("forgotPassword: unknown email still returns success (anti-enumeration)")
    void forgotPassword_unknownEmail_returnsSuccessWithoutSendingEmail() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        var result = service.forgotPassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ForgotPasswordCommand("nobody@example.com"));

        assertThat(result.isSuccess()).isTrue();
        verify(emailService, org.mockito.Mockito.never()).sendPasswordResetEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("resetPassword: invalid token returns failure")
    void resetPassword_invalidToken_returnsFailure() {
        when(userRepository.findByResetToken("bad-token")).thenReturn(Optional.empty());
        var result = service.resetPassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand("bad-token", "newpass123"));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("resetPassword: expired token returns failure")
    void resetPassword_expiredToken_returnsFailure() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        user.setResetToken("tok");
        user.setResetTokenExpiresAt(java.time.Instant.now().minusSeconds(60));
        when(userRepository.findByResetToken("tok")).thenReturn(Optional.of(user));

        var result = service.resetPassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand("tok", "newpass123"));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("resetPassword: valid token updates password and clears the token")
    void resetPassword_validToken_updatesPassword() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        user.setResetToken("tok");
        user.setResetTokenExpiresAt(java.time.Instant.now().plusSeconds(600));
        when(userRepository.findByResetToken("tok")).thenReturn(Optional.of(user));
        when(hashingService.encode("newpass123")).thenReturn("encoded-new");
        when(userRepository.save(user)).thenReturn(user);

        var result = service.resetPassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand("tok", "newpass123"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(user.getPassword()).isEqualTo("encoded-new");
        assertThat(user.getResetToken()).isNull();
    }

    // ── updateProfile / changePassword ───────────────────────────────────────

    @Test
    @DisplayName("updateProfile: updates fullName and returns the saved user")
    void updateProfile_updatesFullName() {
        User user = new User("farmer@example.com", "hash", "Old Name");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = service.updateProfile(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.UpdateProfileCommand(1L, "New Name"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(user.getFullName()).isEqualTo("New Name");
    }

    @Test
    @DisplayName("changePassword: incorrect current password returns failure")
    void changePassword_wrongCurrentPassword_returnsFailure() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hashingService.matches("wrong", "hash")).thenReturn(false);

        var result = service.changePassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ChangePasswordCommand(1L, "wrong", "newpass123"));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("changePassword: correct current password updates to the new hash")
    void changePassword_correctCurrentPassword_updatesPassword() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(hashingService.matches("correct", "hash")).thenReturn(true);
        when(hashingService.encode("newpass123")).thenReturn("encoded-new");
        when(userRepository.save(user)).thenReturn(user);

        var result = service.changePassword(new com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ChangePasswordCommand(1L, "correct", "newpass123"));

        assertThat(result.isSuccess()).isTrue();
        assertThat(user.getPassword()).isEqualTo("encoded-new");
    }

    // ── sign-in blocked account / setBlocked ─────────────────────────────────

    @Test
    @DisplayName("handle(SignInCommand): blocked user returns failure")
    void signIn_blockedUser_returnsFailure() {
        User user = new User("blocked@example.com", "hash", "User");
        user.setVerified(true);
        user.setBlocked(true);
        when(userRepository.findByEmail("blocked@example.com")).thenReturn(Optional.of(user));
        when(hashingService.matches("pass", "hash")).thenReturn(true);

        var result = service.handle(new SignInCommand("blocked@example.com", "pass"));

        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("setBlocked: blocks an existing user")
    void setBlocked_existingUser_blocksAndSaves() {
        User user = new User("farmer@example.com", "hash", "Farmer");
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        var result = service.setBlocked(1L, true);

        assertThat(result.isSuccess()).isTrue();
        assertThat(user.isBlocked()).isTrue();
    }

    @Test
    @DisplayName("setBlocked: unknown user returns failure")
    void setBlocked_unknownUser_returnsFailure() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        var result = service.setBlocked(99L, true);
        assertThat(result.isFailure()).isTrue();
    }
}
