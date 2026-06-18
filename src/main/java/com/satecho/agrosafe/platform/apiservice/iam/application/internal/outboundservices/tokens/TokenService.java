package com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.tokens;

/**
 * Outbound port for bearer token issuance and validation used by IAM commands and queries.
 */
public interface TokenService {

    /**
     * Generate a token for a given email
     *
     * @param email the email
     * @return String the token
     */
    String generateToken(String email);

    /**
     * Extracts the email from a token
     *
     * @param token token value
     * @return email embedded in the token
     */
    String getEmailFromToken(String token);

    /**
     * Validates a token
     *
     * @param token token value
     * @return {@code true} when token is valid; otherwise {@code false}
     */
    boolean validateToken(String token);
}