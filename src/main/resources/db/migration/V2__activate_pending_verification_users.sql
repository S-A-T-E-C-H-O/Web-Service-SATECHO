-- One-off data fix: activate accounts stuck in PENDING_VERIFICATION.
-- Guarded so it is a no-op on a fresh database where users does not exist yet
-- (Flyway runs before Hibernate creates the schema).
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = current_schema() AND table_name = 'users'
    ) THEN
        UPDATE users
        SET verified = true,
            verification_status = 'VERIFIED',
            verification_token = NULL,
            verification_token_expires_at = NULL,
            verification_last_sent_at = NULL,
            verification_resend_count = 0
        WHERE verified = false
           OR verification_status = 'PENDING_VERIFICATION';
    END IF;
END $$;
