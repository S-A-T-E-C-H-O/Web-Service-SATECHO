package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordResource(@NotBlank @Email String email) {
}
