UPDATE users
SET verified = true,
    verification_status = 'VERIFIED',
    verification_token = NULL,
    verification_token_expires_at = NULL,
    verification_last_sent_at = NULL,
    verification_resend_count = 0
WHERE verified = false
   OR verification_status = 'PENDING_VERIFICATION';
