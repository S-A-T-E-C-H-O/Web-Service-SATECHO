package com.satecho.agrosafe.platform.apiservice.iam.infrastructure.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.satecho.agrosafe.platform.apiservice.iam.application.internal.outboundservices.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResendEmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(ResendEmailServiceImpl.class);

    private final Resend resend;
    private final String from;
    private final String baseUrl;

    public ResendEmailServiceImpl(
            @Value("${resend.api-key}") String apiKey,
            @Value("${resend.from:onboarding@resend.dev}") String from,
            @Value("${app.base-url:http://localhost:8080}") String baseUrl) {
        this.resend = new Resend(apiKey);
        this.from = from;
        this.baseUrl = baseUrl;
    }

    @Override
    public void sendVerificationEmail(String to, String fullName, String verificationToken) {
        // TODO: cuando el frontend esté desplegado, reemplazar baseUrl por app.frontend-url
        //       y cambiar la ruta a: "${frontendUrl}/verify-account?token=" + verificationToken
        var verificationUrl = baseUrl + "/api/v1/authentication/verify-account?token=" + verificationToken;
        var options = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject("Verifica tu cuenta en AgroSafe")
                .html(buildHtml(fullName, verificationUrl))
                .build();
        try {
            resend.emails().send(options);
        } catch (ResendException e) {
            log.error("Failed to send verification email to {}: {}", to, e.getMessage());
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String fullName, String resetToken) {
        // TODO: cuando el frontend esté desplegado, reemplazar baseUrl por app.frontend-url
        //       y cambiar la ruta a: "${frontendUrl}/reset-password?token=" + resetToken
        var resetUrl = baseUrl + "/api/v1/authentication/reset-password?token=" + resetToken;
        var options = CreateEmailOptions.builder()
                .from(from)
                .to(to)
                .subject("Recupera tu contraseña en AgroSafe")
                .html(buildResetHtml(fullName, resetUrl))
                .build();
        try {
            resend.emails().send(options);
        } catch (ResendException e) {
            log.error("Failed to send password reset email to {}: {}", to, e.getMessage());
        }
    }

    private String buildResetHtml(String fullName, String resetUrl) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"></head>
                <body style="margin:0;padding:0;background:#f4f4f5;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',sans-serif">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f4f5;padding:40px 0">
                    <tr><td align="center">
                      <table width="520" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,0.08)">
                        <tr>
                          <td style="background:#16a34a;padding:28px 40px;text-align:center">
                            <span style="font-size:28px">&#127807;</span>
                            <span style="display:block;color:#ffffff;font-size:22px;font-weight:700;letter-spacing:-0.5px;margin-top:6px">AgroSafe</span>
                            <span style="color:#bbf7d0;font-size:13px">Monitoreo agrícola inteligente</span>
                          </td>
                        </tr>
                        <tr>
                          <td style="padding:40px 40px 32px">
                            <p style="margin:0 0 8px;color:#111827;font-size:20px;font-weight:600">Hola, %s 👋</p>
                            <p style="margin:0 0 28px;color:#6b7280;font-size:15px;line-height:1.6">
                              Recibimos una solicitud para restablecer tu contraseña. Este enlace vence en 1 hora.
                              Si no fuiste tú, ignora este correo.
                            </p>
                            <table cellpadding="0" cellspacing="0">
                              <tr>
                                <td style="background:#16a34a;border-radius:8px">
                                  <a href="%s"
                                     style="display:inline-block;color:#ffffff;font-size:15px;font-weight:600;
                                            text-decoration:none;padding:14px 32px;letter-spacing:0.2px">
                                    Restablecer contraseña
                                  </a>
                                </td>
                              </tr>
                            </table>
                            <p style="margin:24px 0 0;color:#9ca3af;font-size:12px;line-height:1.5">
                              Si el botón no funciona, copia este enlace en tu navegador:<br>
                              <a href="%s" style="color:#16a34a;word-break:break-all">%s</a>
                            </p>
                          </td>
                        </tr>
                        <tr>
                          <td style="border-top:1px solid #f0f0f0;padding:20px 40px;text-align:center">
                            <p style="margin:0;color:#d1d5db;font-size:12px">© 2025 AgroSafe · Satecho · Todos los derechos reservados</p>
                          </td>
                        </tr>
                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(fullName, resetUrl, resetUrl, resetUrl);
    }

    private String buildHtml(String fullName, String verificationUrl) {
        return """
                <!DOCTYPE html>
                <html lang="es">
                <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1"></head>
                <body style="margin:0;padding:0;background:#f4f4f5;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',sans-serif">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f4f5;padding:40px 0">
                    <tr><td align="center">
                      <table width="520" cellpadding="0" cellspacing="0" style="background:#ffffff;border-radius:12px;overflow:hidden;box-shadow:0 1px 4px rgba(0,0,0,0.08)">

                        <!-- Header -->
                        <tr>
                          <td style="background:#16a34a;padding:28px 40px;text-align:center">
                            <span style="font-size:28px">&#127807;</span>
                            <span style="display:block;color:#ffffff;font-size:22px;font-weight:700;letter-spacing:-0.5px;margin-top:6px">AgroSafe</span>
                            <span style="color:#bbf7d0;font-size:13px">Monitoreo agrícola inteligente</span>
                          </td>
                        </tr>

                        <!-- Body -->
                        <tr>
                          <td style="padding:40px 40px 32px">
                            <p style="margin:0 0 8px;color:#111827;font-size:20px;font-weight:600">Hola, %s 👋</p>
                            <p style="margin:0 0 28px;color:#6b7280;font-size:15px;line-height:1.6">
                              Verifica tu correo para comenzar a monitorear tus cultivos en tiempo real.
                              Un clic y tendrás acceso completo a tu panel.
                            </p>
                            <table cellpadding="0" cellspacing="0">
                              <tr>
                                <td style="background:#16a34a;border-radius:8px">
                                  <a href="%s"
                                     style="display:inline-block;color:#ffffff;font-size:15px;font-weight:600;
                                            text-decoration:none;padding:14px 32px;letter-spacing:0.2px">
                                    Verificar cuenta
                                  </a>
                                </td>
                              </tr>
                            </table>
                            <p style="margin:24px 0 0;color:#9ca3af;font-size:12px;line-height:1.5">
                              Si el botón no funciona, copia este enlace en tu navegador:<br>
                              <a href="%s" style="color:#16a34a;word-break:break-all">%s</a>
                            </p>
                          </td>
                        </tr>

                        <!-- Footer -->
                        <tr>
                          <td style="border-top:1px solid #f0f0f0;padding:20px 40px;text-align:center">
                            <p style="margin:0;color:#d1d5db;font-size:12px">© 2025 AgroSafe · Satecho · Todos los derechos reservados</p>
                          </td>
                        </tr>

                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(fullName, verificationUrl, verificationUrl, verificationUrl);
    }
}