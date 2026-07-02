package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ForgotPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResetPasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.ForgotPasswordResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.ResetPasswordResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.SignInResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.SignUpResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication operations (sign-up and sign-in).
 */
@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Sign-up and sign-in endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Sign up a new user.
     * POST /api/v1/authentication/sign-up
     */
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpResource resource) {
        var signUpCommand = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(signUpCommand);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                UserResourceFromEntityAssembler::toResourceFromEntity,
                HttpStatus.CREATED
        );
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInResource resource) {
        var signInCommand = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(signInCommand);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                auth -> AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(auth.getLeft(), auth.getRight()),
                HttpStatus.OK
        );
    }

    @GetMapping("/verify-account")
    public ResponseEntity<?> verifyAccount(@RequestParam String token) {
        var command = new VerifyAccountCommand(token);
        var result = userCommandService.verifyAccount(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> body) {
        var email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("message", "email is required"));
        }
        var command = new ResendVerificationCommand(email);
        var result = userCommandService.resendVerification(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, v -> Map.of("message", "Verification email sent"), HttpStatus.OK);
    }

    /**
     * Always responds 200 regardless of whether the email exists (anti-enumeration).
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordResource resource) {
        var result = userCommandService.forgotPassword(new ForgotPasswordCommand(resource.email()));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, v -> Map.of("message", "If the email exists, a reset link has been sent"), HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordResource resource) {
        var command = new ResetPasswordCommand(resource.token(), resource.newPassword());
        var result = userCommandService.resetPassword(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, v -> Map.of("message", "Password updated successfully"), HttpStatus.OK);
    }
}