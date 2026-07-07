-- Adds the verification lifecycle columns to users.
--
-- Idempotent / fresh-database safe: the base schema is created by Hibernate
-- (ddl-auto) on first boot, and Flyway runs BEFORE Hibernate. On a brand-new
-- database the users table does not exist yet, so this migration must be a
-- no-op there — Hibernate then creates users already including these columns
-- (they are mapped on the entity). On databases that predate the columns,
-- the ALTERs below apply normally.
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.tables
        WHERE table_schema = current_schema() AND table_name = 'users'
    ) THEN
        ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_status varchar(32);
        ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_token_expires_at timestamp with time zone;
        ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_last_sent_at timestamp with time zone;
        ALTER TABLE users ADD COLUMN IF NOT EXISTS verification_resend_count integer NOT NULL DEFAULT 0;

        UPDATE users
        SET verification_status = CASE WHEN verified THEN 'VERIFIED' ELSE 'PENDING_VERIFICATION' END
        WHERE verification_status IS NULL;

        ALTER TABLE users ALTER COLUMN verification_status SET NOT NULL;

        IF EXISTS (
            SELECT 1 FROM information_schema.columns
            WHERE table_schema = current_schema()
              AND table_name = 'users' AND column_name = 'verification_token'
        ) THEN
            CREATE INDEX IF NOT EXISTS idx_users_verification_token ON users (verification_token);
        END IF;
    END IF;
END $$;
