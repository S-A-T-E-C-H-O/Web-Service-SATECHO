package com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.email;

public interface EmailService {
    void sendVerificationEmail(String to, String fullName, String verificationToken);
}