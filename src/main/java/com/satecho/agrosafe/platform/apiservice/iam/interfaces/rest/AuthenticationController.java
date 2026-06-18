package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ResendVerificationCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.VerifyAccountCommand;
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
}