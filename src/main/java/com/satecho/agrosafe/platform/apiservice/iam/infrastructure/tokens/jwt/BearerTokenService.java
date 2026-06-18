package com.satecho.agrosafe.platform.apiservice.iam.infrastructure.tokens.jwt;

import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.tokens.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * Marker interface extending {@link TokenService} with bearer token extraction.
 */
public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);
    String generateToken(Authentication authentication);
}